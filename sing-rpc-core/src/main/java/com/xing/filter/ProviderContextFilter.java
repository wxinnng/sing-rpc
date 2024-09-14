package com.xing.filter;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 生产端的过滤器
 */
@Slf4j
public class ProviderContextFilter implements ProviderFilter{

    @Override
    public Integer getType() {
        return FilterKeys.PROVIDER_FILTER;
    }

    @Override
    public void doFilter(RpcRequest request, RpcResponse response, FilterChain filterChain) {
        log.info("providerContextFilter -- 前置处理");
        filterChain.doFilter(request,response);
        log.info("providerContextFilter -- 后置处理");
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_PROVIDER_FILTER_ORDER;
    }
}
