package com.xing.filter;

import com.xing.RpcApplication;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.spi.SpiLoader;

import java.util.*;

public class ProviderFilterChain implements FilterChain{

    private static final PriorityQueue<Filter> PROVIDER_FILTER_SET = new PriorityQueue<>(Comparator.comparingInt(Filter::getOrder));

    private final Iterator<Filter> filterIterator = PROVIDER_FILTER_SET.iterator();



    //加载实例到对应的集合中去。
    static{
        //加载所有的实现类
        SpiLoader.load(ProviderFilter.class);

        Map<String, Class<?>> allClazzByClassName = SpiLoader.getAllClazzByClassName(ProviderFilter.class.getName());

        Set<String> filterExclusionSet = RpcApplication.getRpcConfig().getFilterExclusionSet();

        for(String key: allClazzByClassName.keySet()){
            if(! filterExclusionSet.contains(key)){

                //创建实例
                Filter filter = FilterFactory.getInstance(ProviderFilter.class, key);
                //放到队列中
                PROVIDER_FILTER_SET.add(filter);
            }
        }
    }

    /**
     * 执行提供者的过滤器链
     * @param rpcRequest
     * @param rpcResponse
     * @return
     */
    public void doFilter(RpcRequest rpcRequest, RpcResponse rpcResponse){
        //后面没有过滤器了，就直接返回
        if(!filterIterator.hasNext()){
            return ;
        }
        //拿到下一个过滤器
        Filter filter = filterIterator.next();
        //执行过滤器
        filter.doFilter(rpcRequest,rpcResponse,this);
    }
}