package com.xing.proxy;

import com.xing.RpcApplication;
import com.xing.model.DiscoverParams;

import java.lang.reflect.Proxy;

import static java.lang.reflect.Proxy.newProxyInstance;

public class ServiceProxyFactory {

    /**
     * 获得对应服务的代理对象
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> tClass, DiscoverParams discoverParams){

        //如果放的是null，就使用默认值,这里主要针对的是不使用starter的情况，版本信息需要通过配置文件来弄
        if(discoverParams == null){
            discoverParams = new DiscoverParams();
        }

        //开启了mock测试直接返回mock对象就好,这里是兼容非starter模式下
        //局部的配置的优先级要高于全局配置
        if(discoverParams.getMock() || RpcApplication.getRpcConfig().isMock() ){
            return getMockProxy(tClass);
        }

        //返回代理对象。
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass}, new ServiceProxy(discoverParams));
    }


    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }

}
