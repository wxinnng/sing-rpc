package com.xing.filter;

import com.xing.spi.SpiLoader;

public class FilterFactory {
    static{

    }

    /**
     * 获得一个filter实例
     * @param key
     * @return
     */
    public static Filter getInstance(String key){
        return SpiLoader.getInstance(Filter.class,key);
    }


}
