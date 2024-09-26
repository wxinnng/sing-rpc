package io.github.wxinnng.cache.service;

import java.util.Map;

public class ServiceCache {
    private static final Map<String,Object> serviceCache = new java.util.concurrent.ConcurrentHashMap<>();

    public static Object get(String key){
        return serviceCache.get(key);
    }
    public static void put(String key,Object value){
        serviceCache.put(key, value);
    }



}
