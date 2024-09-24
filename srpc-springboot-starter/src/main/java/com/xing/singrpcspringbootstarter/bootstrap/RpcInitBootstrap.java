package com.xing.singrpcspringbootstarter.bootstrap;

import com.xing.RpcApplication;
import com.xing.config.RpcConfig;
import com.xing.model.ServiceMetaInfo;
import com.xing.model.ServiceRegisterInfo;
import com.xing.registry.LocalRegistry;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.server.tcp.VertxTcpServer;
import com.xing.service.SystemService;
import com.xing.service.SystemServiceFactory;
import com.xing.singrpcspringbootstarter.annotation.EnableSingRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Rpc框架启动
 *
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableRpc注解属性
        Boolean needServer = (Boolean) importingClassMetadata.getAnnotationAttributes(EnableSingRpc.class.getName()).get("needServer");

        //RPC初始化
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //拿到远程注册中心，注册与系统相关的服务
        Registry remoteRegistry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        remoteRegistry.registryOtherMessage();

        //启动服务器
        if(needServer){
            log.info("启动服务器");
            VertxTcpServer server = new VertxTcpServer();
            server.doStart(rpcConfig.getServerPort());
        }else{
            log.info("不需要启动服务器");
        }
    }
}
