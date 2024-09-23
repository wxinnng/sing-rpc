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


        if(rpcConfig.getSrsm()){
            log.info("加载srsm相关的系统服务");
            //拿到系统服务实现类的class类
            Class<? extends SystemService> aclass = SystemServiceFactory.getInstanceClass(rpcConfig.getRegistryConfig().getRegistry());
            //封装一个服务
            ServiceRegisterInfo<SystemService> systemServiceServiceRegisterInfo = new ServiceRegisterInfo<>();
            systemServiceServiceRegisterInfo.setServiceName(SystemService.class.getName());
            systemServiceServiceRegisterInfo.setImplClass(aclass);

            String serviceName = systemServiceServiceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName,systemServiceServiceRegisterInfo.getImplClass());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            //服务的token
            String token = RpcApplication.getRpcConfig().getToken();
            serviceMetaInfo.setToken(token);

            //放到服务列表中，在后面进行注册
            Registry remoteRegistry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());

            try{
                remoteRegistry.register(serviceMetaInfo);
            }catch (Exception e){
                throw new RuntimeException("服务注册失败！",e);
            }
        }

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
