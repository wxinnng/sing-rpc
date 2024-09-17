package com.xing.bootstrap;

import com.xing.RpcApplication;
import com.xing.config.RegistryConfig;
import com.xing.config.RpcConfig;
import com.xing.model.ServiceMetaInfo;
import com.xing.model.ServiceRegisterInfo;
import com.xing.registry.LocalRegistry;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.server.tcp.VertxTcpServer;
import com.xing.service.SystemService;
import com.xing.service.SystemServiceFactory;
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
        //判断是否需要注册系统服务
        if(rpcConfig.getSrsm()){
            log.info("加载srsm相关的系统服务");

            //拿到系统服务实现类的class类
            Class<? extends SystemService> aclass = SystemServiceFactory.getInstanceClass(rpcConfig.getRegistryConfig().getRegistry());
            //封装一个服务
            ServiceRegisterInfo<SystemService> systemServiceServiceRegisterInfo = new ServiceRegisterInfo<>();
            systemServiceServiceRegisterInfo.setServiceName(SystemService.class.getName());
            systemServiceServiceRegisterInfo.setImplClass(aclass);
            //放到服务列表中，在后面进行注册
            serviceRegisterInfoList.add(systemServiceServiceRegisterInfo);
        }
        //注册服务
        for(ServiceRegisterInfo<?> serviceRegisterInfo: serviceRegisterInfoList){
            //服务名称
            String serviceName = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());
            //注册服务到服务中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            //拿到对应的注册中心的实例
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            //服务信息
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
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
    }
}
