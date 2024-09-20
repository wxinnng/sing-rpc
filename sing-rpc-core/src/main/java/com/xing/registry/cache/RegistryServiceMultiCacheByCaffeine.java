package com.xing.registry.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.xing.RpcApplication;
import com.xing.model.ServiceMetaInfo;
import com.xing.registry.Registry;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RegistryServiceMultiCacheByCaffeine {



    //基于caffeine的本地注册中心
    LoadingCache<String, List<ServiceMetaInfo>> serviceCache = null;

    //至于怎么获得实例，由不同的远程注册中心决定。
    public RegistryServiceMultiCacheByCaffeine(Registry registry){

        //判断是否配置了，时间过期策略
        if (RpcApplication.getRpcConfig().getRegistryConfig().getLRU()){
            Long seconds = RpcApplication.getRpcConfig().getRegistryConfig().getExpireTime();
            log.info("初始化本地服务注册中心，使用LRU过期策略，时间周期为 {} s",seconds);

            serviceCache = Caffeine.newBuilder()
                    .maximumSize(RpcApplication.getRpcConfig().getRegistryConfig().getMaxService())
                    .expireAfterAccess(seconds, TimeUnit.SECONDS)
                    .build(registry::getServiceInstance);
        }else{
            log.info("初始化本地服务中心，不使用过期策略");
            serviceCache = Caffeine.newBuilder()
                    .maximumSize(RpcApplication.getRpcConfig().getRegistryConfig().getMaxService())
                    .build(registry::getServiceInstance);
        }
    }
    /**
     * 写缓存
     *
     * @param serviceKey 服务键名
     * @param newServiceCache 更新后的缓存列表
     * @return
     */
    public void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        //使用caffeine之后，缓存的写入由对应的远程服务注册中信自己实现 ===> registry::getServiceInstance
    }

    /**
     * 读缓存
     *
     * @param serviceKey
     * @return
     */
    public List<ServiceMetaInfo> readCache(String serviceKey) {
        //service -> group & version -> service list
        return serviceCache.get(serviceKey);
    }

    /**
     * 清除
     */
    public void removeCache(String serviceKey) {
        serviceCache.invalidate(serviceKey);
    }


}
