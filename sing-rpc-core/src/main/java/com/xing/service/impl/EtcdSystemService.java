package com.xing.service.impl;

import cn.hutool.json.JSONUtil;
import com.xing.RpcApplication;
import com.xing.constant.RpcConstant;
import com.xing.model.RemoteServiceInfo;
import com.xing.model.ServiceManagementInfo;
import com.xing.model.ServiceMetaInfo;
import com.xing.model.ServiceStrategyInfo;
import com.xing.registry.Registry;
import com.xing.registry.RegistryKeys;
import com.xing.service.SystemService;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.options.GetOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static io.netty.util.ThreadDeathWatcher.watch;
@Slf4j
public class EtcdSystemService implements SystemService {

    private static KV kvClient;
    public static void setClient(KV client){
        EtcdSystemService.kvClient = client;
    }
    @Override
    public List<RemoteServiceInfo> getServiceInfo() {
        return getServiceInfoFromRemoteRegistry();
    }
    @Override
    public List<ServiceManagementInfo> getServiceManagementInfo() {
        List<ServiceManagementInfo> result = null;
        List<RemoteServiceInfo> serviceMetaInfo = getServiceInfoFromRemoteRegistry();

        if (serviceMetaInfo == null || serviceMetaInfo.isEmpty())
            return null;
        //进行操作
        Map<String, List<RemoteServiceInfo>> map = new HashMap<>();
        //serviceName, RemoteServiceInfo
        for (RemoteServiceInfo info : serviceMetaInfo) {
            String key = info.getServiceName();
            List<RemoteServiceInfo> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(info);
        }
        result = map.entrySet().stream().map(entry -> {
            ServiceManagementInfo serviceManagementInfo = new ServiceManagementInfo();
            serviceManagementInfo.setServiceName(entry.getKey());
            serviceManagementInfo.setStatus(1);
            serviceManagementInfo.setVersionCount(1);
            serviceManagementInfo.setInstanceCount(entry.getValue().size());
            serviceManagementInfo.setGroupCount(1);
            serviceManagementInfo.setRemoteRegistry(RpcApplication.getRpcConfig().getRegistryConfig().getAddress());
            return serviceManagementInfo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<ServiceStrategyInfo> getServiceStrategyInfo() {
        List<KeyValue> keyValues = doSearchByKey(RegistryKeys.SRSM_ROOT_PATH);
        return keyValues.stream()
                .map(keyValue -> {
                    String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                    return JSONUtil.toBean(value, ServiceStrategyInfo.class);
                })
                .collect(Collectors.toList());
    }


    private List<KeyValue> doSearchByKey(String key){
        GetOption getOption = GetOption.builder().isPrefix(true).build();
        try {
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(key, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            return keyValues;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<RemoteServiceInfo> getServiceInfoFromRemoteRegistry(){
        List<RemoteServiceInfo> serviceMetaInfo = null;
        // 前缀查询
        try{
            List<KeyValue> keyValues = doSearchByKey(RegistryKeys.ETCD_ROOT_PATH);
            // 解析服务信息
            serviceMetaInfo = keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, RemoteServiceInfo.class);
                    })
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("远程服务调用异常！{}",e.getMessage());
        }
        return serviceMetaInfo;
    }
}
