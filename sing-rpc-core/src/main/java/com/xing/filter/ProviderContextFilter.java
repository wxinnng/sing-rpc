package com.xing.filter;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 生产端的过滤器
 */
@Slf4j
public class ProviderContextFilter implements Filter{

    @Override
    public Integer getType() {
        return FilterKeys.PROVIDER_FILTER;
    }

    @Override
    public boolean doFilter(RpcRequest request, RpcResponse response) {
        log.info("经过 providerContextFilter");
        return true;
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_PROVIDER_FILTER_ORDER;
    }
}
