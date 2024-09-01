package com.xing.proxy;

import com.xing.RpcApplication;

import java.lang.reflect.Proxy;

import static java.lang.reflect.Proxy.newProxyInstance;

public class ServiceProxyFactory {

    /**
     * 获得对应服务的代理对象
     * @param tClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> tClass){
        if(RpcApplication.getRpcConfig().isMock()){
            return getMockProxy(tClass);
        }
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass}, new ServiceProxy());
    }


    public static <T> T getMockProxy(Class<T> serviceClass){
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new MockServiceProxy()
        );
    }

}
