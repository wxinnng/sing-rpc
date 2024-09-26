package io.github.wxinnng.singrpcspringbootstarter.bootstrap;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.config.RpcConfig;
import io.github.wxinnng.registry.Registry;
import io.github.wxinnng.registry.RegistryFactory;
import io.github.wxinnng.server.tcp.VertxTcpServer;
import io.github.wxinnng.singrpcspringbootstarter.annotation.EnableSingRpc;
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
