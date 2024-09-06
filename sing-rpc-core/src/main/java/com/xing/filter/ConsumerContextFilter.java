package com.xing.filter;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者端的过滤器
 */
@Slf4j
public class ConsumerContextFilter implements Filter {

    @Override
    public boolean doFilter(RpcRequest request, RpcResponse response) {
        log.info("经过consumerContextFilter");
        return true;
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
