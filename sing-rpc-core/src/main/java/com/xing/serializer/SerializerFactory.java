package com.xing.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化工厂:序列化器，不用每次使用的时候就创建一个，单例+工厂
 */
public class SerializerFactory {

    /**
     * 序列化器集合
     */
    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<>(){{
        put(SerializerKeys.HESSIAN, new HessianSerializer());
        put(SerializerKeys.JSON, new JSONSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.JDK, new JDKSerializer());
    }};

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get(SerializerKeys.JDK);

    /**
     * 获取序列化器
     *
     * @param key 序列化器key
     * @return 序列化器
     */
    public static Serializer getInstance(String key) {
        // 不存在的key，默认使用JDK序列化器
        return KEY_SERIALIZER_MAP.getOrDefault(key, DEFAULT_SERIALIZER);
    }
}
