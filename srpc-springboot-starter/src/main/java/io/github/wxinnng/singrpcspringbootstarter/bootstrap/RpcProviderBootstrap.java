package io.github.wxinnng.singrpcspringbootstarter.bootstrap;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.config.RegistryConfig;
import io.github.wxinnng.config.RpcConfig;
import io.github.wxinnng.model.ServiceMetaInfo;
import io.github.wxinnng.registry.LocalRegistry;
import io.github.wxinnng.registry.Registry;
import io.github.wxinnng.registry.RegistryFactory;
import io.github.wxinnng.singrpcspringbootstarter.annotation.SingRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

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
            serviceMetaInfo.setServiceGroup(rpcService.group());
            serviceMetaInfo.setServiceVersion(serviceVersion == null ? rpcService.version() : serviceVersion);
            String token = RpcApplication.getRpcConfig().getToken();
            serviceMetaInfo.setToken(token);

            try {

                //注册用户服务
                registry.register(serviceMetaInfo);

                log.info("服务注册成功");
            } catch (Exception e) {
                log.error("注册服务失败",e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
