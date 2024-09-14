package com.xing.filter;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者端的过滤器
 */
@Slf4j
public class ConsumerContextFilter implements ConsumerFilter {

    @Override
    public void doFilter(RpcRequest rpcRequest, RpcResponse response, FilterChain filterChain) {
        filterChain.doFilter(rpcRequest,response);
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_CONSUMER_FILTER_ORDER;
    }

    @Override
    public Integer getType() {
        return FilterKeys.CONSUMER_FILTER;
    }


}
