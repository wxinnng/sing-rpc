package com.xing.registry;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;

import com.xing.config.RegistryConfig;
import com.xing.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EtcdRegistry implements Registry {


    private final Set<String> localRegistryNodeKeySet = new HashSet<>();

    private Client client;

    private KV kvClient;


    /**
     * 正在监听的 key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    //缓存服务信息
//    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    private final RegistryServiceMultiCache registryServiceCache = new RegistryServiceMultiCache();

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();

        //开启心跳检测方法
        this.heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        // key :  rpc/service/group/version/ host:port value: serviceInfo
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();

        kvClient.put(key, value, putOption).get();

        //将注册的key放到set中去
        localRegistryNodeKeySet.add(registerKey);

    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String serviceNodeKey = serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceNodeKey, StandardCharsets.UTF_8));
        localRegistryNodeKeySet.remove(serviceNodeKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        //优先从缓存中获取服务
        List<ServiceMetaInfo> serviceCache = registryServiceCache.readCache(serviceKey);
        if(serviceCache != null) {
            return serviceCache;
        }


        // 前缀搜索，结尾一定要加 '/' /rpc/service/group/version/ 下面可能会有多个实例。
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfo = keyValues.stream()
                    .map(keyValue -> {
                        //监听key变化
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);

                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());

            //将读取的内容写入缓存
            registryServiceCache.writeCache(serviceKey , serviceMetaInfo);
            //返回缓存内容
            return serviceMetaInfo;

        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }


    @Override
    public void destroy() {
        //服务机主动下线
        for(String key:localRegistryNodeKeySet){
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }


    @Override
    public void heartBeat() {
        //Hutool的定时工具
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                //遍历所有的节点。
                for(String key:localRegistryNodeKeySet){
                    try{
                        //serviceKey: 是服务名 + 版本号                            serviceNodeKey :host + port
                        List<KeyValue> kvs = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8)).get().getKvs();
                        //如果节点过期，那么需要服务重新开启
                        if(CollUtil.isEmpty(kvs)){
                            continue;
                        }
                        //节点未过期，重新注册
                        KeyValue keyValue = kvs.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        //重新注册一次。
                        register(serviceMetaInfo);

                    }catch(Exception e){
                        throw new RuntimeException(key + "续约失败！" + e.getMessage());
                    }
                }
            }
        });

        //支持毫秒级别任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();

    }

    /**
     * serviceNodeKey监听
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        //如果没有被监听过，就进行监听
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if(newWatch){
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8),response ->{
                for (WatchEvent event : response.getEvents()){
                    switch (event.getEventType()){
                        case DELETE:
                            //如果对这个key是删除操作，就要清理一次缓存
                            registryServiceCache.clearCache(serviceNodeKey);
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }
}
