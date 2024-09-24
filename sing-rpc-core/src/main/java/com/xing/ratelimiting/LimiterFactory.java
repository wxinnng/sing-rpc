package com.xing.ratelimiting;

import com.xing.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LimiterFactory {

    static {
        log.info("加载限流操作。");
        //加载对应的限流策略
        SpiLoader.load(Limiter.class);
    }


    /**
     * 获取对应的实例
     * @param key
     * @return
     */
    public static Limiter getInstance(String key)
    {
        return SpiLoader.getInstance(Limiter.class, key);
    }
}
