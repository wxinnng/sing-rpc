package com.xing.filter;


import com.xing.RpcApplication;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.spi.SpiLoader;

import java.util.*;

public class FilterChain {


    private static final PriorityQueue<Filter> CONSUMER_FILTER_SET = new PriorityQueue<>(Comparator.comparingInt(Filter::getOrder));

    private static final PriorityQueue<Filter> PROVIDER_FILTER_SET = new PriorityQueue<>(Comparator.comparingInt(Filter::getOrder));

    //加载实例到对应的集合中去。
    static{
        //加载所有的实现类
        SpiLoader.load(Filter.class);

        Map<String, Class<?>> allClazzByClassName = SpiLoader.getAllClazzByClassName(Filter.class.getName());

        Set<String> filterExclusionSet = RpcApplication.getRpcConfig().getFilterExclusionSet();

        for(String key: allClazzByClassName.keySet()){
            if(! filterExclusionSet.contains(key)){

                //创建实例
                Filter filter = FilterFactory.getInstance(key);

                if(Objects.equals(filter.getType(), FilterKeys.CONSUMER_FILTER)){
                    CONSUMER_FILTER_SET.add(filter);
                }else if(Objects.equals(filter.getType(),FilterKeys.PROVIDER_FILTER)){
                    PROVIDER_FILTER_SET.add(filter);
                }else{
                    PROVIDER_FILTER_SET.add(filter);
                    CONSUMER_FILTER_SET.add(filter);
                }
            }
        }
    }

    private static PriorityQueue<Filter> getProviderFilter(){
        return PROVIDER_FILTER_SET;
    }
    private static PriorityQueue<Filter> getConsumerFilter(){
        return CONSUMER_FILTER_SET;
    }


    /**
     * 执行提供者的过滤器链
     * @param rpcRequest
     * @param rpcResponse
     * @return
     */
    public static boolean doProviderFilter(RpcRequest rpcRequest, RpcResponse rpcResponse){
        for(Filter filter:getProviderFilter()){
            if(!filter.doFilter(rpcRequest,rpcResponse)){
                return false;
            }
        }
        return true;
    }

    /**
     * 执行消费者的过滤器链
     * @param rpcRequest
     * @param rpcResponse
     * @return
     */
    public static boolean doConsumerFilter(RpcRequest rpcRequest, RpcResponse rpcResponse){
        for(Filter filter:getConsumerFilter()){
            if(!filter.doFilter(rpcRequest,rpcResponse)){
                return false;
            }
        }
        return true;
    }

}
