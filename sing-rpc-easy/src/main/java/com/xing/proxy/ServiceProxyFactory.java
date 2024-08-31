package com.xing.proxy;

import java.lang.reflect.Proxy;

import static java.lang.reflect.Proxy.newProxyInstance;

public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> tClass){
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(),new Class[]{tClass}, new ServiceProxy());
    }

}
