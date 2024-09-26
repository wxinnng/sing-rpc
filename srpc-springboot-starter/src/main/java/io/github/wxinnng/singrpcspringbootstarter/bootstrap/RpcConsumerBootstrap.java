package io.github.wxinnng.singrpcspringbootstarter.bootstrap;


import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.constant.RpcConstant;
import io.github.wxinnng.model.DiscoverParams;
import io.github.wxinnng.proxy.ServiceProxyFactory;
import io.github.wxinnng.singrpcspringbootstarter.annotation.SingRpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {


    /**
     * Bean初始化后，执行注册服务
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();

        //遍历对象所有的属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            SingRpcReference rpcReference = field.getAnnotation(SingRpcReference.class);

            if(rpcReference != null){
                //为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if(interfaceClass == void.class){
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);

                //封装服务发现的参数
                DiscoverParams discoverParams = new DiscoverParams();
                discoverParams.setVersion(rpcReference.version());
                discoverParams.setMock(rpcReference.mock());
                if(rpcReference.group().equals(RpcConstant.DEFAULT_GROUP)){
                    //那么就是用配置文件系统的分组配置
                    discoverParams.setGroup(RpcApplication.getRpcConfig().getServiceGroup());
                }else{
                    discoverParams.setGroup(rpcReference.group());
                }

                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass,discoverParams);
                try {
                    field.set(bean,proxyObject);
                    field.setAccessible(true);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("代理对象生成失败！");
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
