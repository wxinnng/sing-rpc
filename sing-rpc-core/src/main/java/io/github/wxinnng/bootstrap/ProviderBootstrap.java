package io.github.wxinnng.bootstrap;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.config.RegistryConfig;
import io.github.wxinnng.config.RpcConfig;
import io.github.wxinnng.model.ServiceMetaInfo;
import io.github.wxinnng.model.ServiceRegisterInfo;
import io.github.wxinnng.registry.LocalRegistry;
import io.github.wxinnng.registry.Registry;
import io.github.wxinnng.registry.RegistryFactory;
import io.github.wxinnng.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 服务提供初始化类
 */
@Slf4j
public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList){
        //RPC 框架初始化
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        //注册服务到服务中心
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        //注册服务
        for(ServiceRegisterInfo<?> serviceRegisterInfo: serviceRegisterInfoList){
            //服务名称
            String serviceName = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());
            //服务信息
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceVersion(rpcConfig.getVersion());
            //服务的token
            String token = RpcApplication.getRpcConfig().getToken();
            serviceMetaInfo.setToken(token);
            //服务的远程注册
            try{
                registry.register(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException("服务注册失败！",e);
            }
            //启动服务器
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        }
        //注册其他的信息信息
        registry.registryOtherMessage();
    }
}
