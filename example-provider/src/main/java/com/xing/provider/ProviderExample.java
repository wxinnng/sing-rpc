package com.xing.provider;


import com.xing.RpcApplication;
import com.xing.config.RegistryConfig;
import com.xing.config.RpcConfig;
import com.xing.model.ServiceMetaInfo;
import com.xing.registry.EtcdRegistry;
import com.xing.registry.LocalRegistry;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.server.HttpServer;
import com.xing.server.VertxHttpServer;
import com.xing.service.UserService;

/**
 * 服务提供者示例
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
