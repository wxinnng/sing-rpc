package io.github.wxinnng.provider;


import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.config.RegistryConfig;
import io.github.wxinnng.config.RpcConfig;
import io.github.wxinnng.model.ServiceMetaInfo;
import io.github.wxinnng.registry.LocalRegistry;
import io.github.wxinnng.registry.Registry;
import io.github.wxinnng.registry.RegistryFactory;
import io.github.wxinnng.server.tcp.VertxTcpServer;
import io.github.wxinnng.service.UserService;

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
        VertxTcpServer tcpServer = new VertxTcpServer();
        tcpServer.doStart(rpcConfig.getServerPort());

    }
}
