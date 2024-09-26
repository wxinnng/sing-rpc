package io.github.wxinnng.fault.tolerant;

import io.github.wxinnng.spi.SpiLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TolerantStrategyFactory {

    static {
        log.info("load tolerantStrategy class by spi");
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认重试机制
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailFastTolerantStrategy();


    /**
     * 获得一个重试机制实例
     * @return
     */
    public static TolerantStrategy getInstance(String key){
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}
