package com.xing.singrpcspringbootstarter.bootstrap;

import com.xing.RpcApplication;
import com.xing.config.RpcConfig;
import com.xing.server.tcp.VertxTcpServer;
import com.xing.singrpcspringbootstarter.annotation.EnableRpc;
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
        Boolean needServer = (Boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        //RPC初始化
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

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
