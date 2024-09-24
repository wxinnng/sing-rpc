package com.xing.singrpcspringbootstarter.bootstrap;

import cn.hutool.core.util.StrUtil;
import com.xing.RpcApplication;
import com.xing.config.RegistryConfig;
import com.xing.config.RpcConfig;
import com.xing.model.ServiceMetaInfo;
import com.xing.model.ServiceRegisterInfo;
import com.xing.registry.LocalRegistry;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.service.SystemService;
import com.xing.service.SystemServiceFactory;
import com.xing.singrpcspringbootstarter.annotation.SingRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.UUID;

/**
 * Rpc服务提供者启动
 */
@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {
    /**
     * 对象初始化后，进行服务注册
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        SingRpcService rpcService = beanClass.getAnnotation(SingRpcService.class);
        if (rpcService != null) {
            //需要注册服务
            //1.获取服务的基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            //默认值处理
            if(interfaceClass == void.class){
                interfaceClass = beanClass.getInterfaces()[0];
            }
            //服务名称
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.version();
            //2.注册服务
            //本地注册
            LocalRegistry.register(serviceName,beanClass);

            //全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();


            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            serviceMetaInfo.setServiceVersion(serviceVersion == null ? rpcService.version() : serviceVersion);
            String token = RpcApplication.getRpcConfig().getToken();
            serviceMetaInfo.setToken(token);

            try {
                registry.register(serviceMetaInfo);
                log.info("服务注册成功");
            } catch (Exception e) {
                log.error("注册服务失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
