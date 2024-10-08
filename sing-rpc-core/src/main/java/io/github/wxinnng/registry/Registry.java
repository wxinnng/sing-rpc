package io.github.wxinnng.registry;

import io.github.wxinnng.config.RegistryConfig;
import io.github.wxinnng.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {



    /**
     * 初始化
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册
     * @param serviceMetaInfo
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务注销
     * @param serviceMetaInfo
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo);


    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceName);


    /**
     * 销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 获得各个分组和版本的实例
     * @param serviceName
     * @return
     */
    List<ServiceMetaInfo> getServiceInstance(String serviceName);


    /**
     * 监听消费者
     */
    void watch(String serviceNodeKey);

    void registryOtherMessage();
}
