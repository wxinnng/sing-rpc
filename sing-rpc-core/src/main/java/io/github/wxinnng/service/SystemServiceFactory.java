package io.github.wxinnng.service;

import io.github.wxinnng.spi.SpiLoader;

import java.util.Map;

public class SystemServiceFactory {
    static {
        Map<String, Class<?>> load = SpiLoader.load(SystemService.class);
    }

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static SystemService getInstance(String key) {
        return SpiLoader.getInstance(SystemService.class, key);
    }

    public static Class<? extends SystemService> getInstanceClass(String key) {
        return (Class<? extends SystemService>) SpiLoader.getAllClazzByClassName(SystemService.class.getName()).get(key);
    }
}
