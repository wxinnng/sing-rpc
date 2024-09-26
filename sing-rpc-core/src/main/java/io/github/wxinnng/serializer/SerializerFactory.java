package io.github.wxinnng.serializer;

import io.github.wxinnng.spi.SpiLoader;

/**
 * 序列化工厂:序列化器，不用每次使用的时候就创建一个，单例  + 工厂
 */
public class SerializerFactory {

    /**
     * 序列化器集合
     */
    static{
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JDKSerializer();

    /**
     * 获取序列化器
     *
     * @param key 序列化器key
     * @return 序列化器
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
