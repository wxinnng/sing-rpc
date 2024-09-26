package io.github.wxinnng.filter;

import io.github.wxinnng.spi.SpiLoader;

public class FilterFactory {
    static{

    }

    /**
     * 获得一个filter实例
     * @param key
     * @return
     */
    public static Filter getInstance(Class tClass,String key){
        return SpiLoader.getInstance(tClass,key);
    }


}
