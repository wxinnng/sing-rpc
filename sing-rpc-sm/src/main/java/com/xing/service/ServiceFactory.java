package com.xing.service;

import com.xing.proxy.ServiceProxyFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂，用于获取服务实例
 */
public class ServiceFactory {
    static {
        try {
            Thread.currentThread().getContextClassLoader().loadClass("com.xing.service.SystemService");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();

    public static<T> T getInstance(Class<T> clazz) {
        //单例模式，双重检验锁
        if(clazz == null)
            return null;
        if(serviceMap.containsKey(clazz)){
            return (T) serviceMap.get(clazz);
        }else{
            //没有就进行加载
            synchronized (ServiceFactory.class){
                //双重检验锁
                if(serviceMap.containsKey(clazz)){
                    return (T) serviceMap.get(clazz);
                }else{
                    //通过sing-rpc的服务工厂，获得实例，放到map中
                    serviceMap.put(clazz,ServiceProxyFactory.getProxy(clazz,null));
                }
            }
        }
        //最终返回对象
        return (T) serviceMap.get(clazz);
    }

}
