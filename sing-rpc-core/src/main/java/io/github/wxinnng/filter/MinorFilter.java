package io.github.wxinnng.filter;

import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinorFilter implements Filter{
    @Override
    public Integer getType() {
        return FilterKeys.MINOR_FILTER;
    }

    @Override
    public void doFilter(RpcRequest request, RpcResponse response, FilterChain filterChain) {
        log.info("minorFilter 前置处理器");

        log.info("minorFilter 后置处理器");
    }

    public boolean doFilter(RpcRequest request, RpcResponse response) {
        log.info("经过MinorFilter");
        return true;
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_MINOR_FILTER_ORDER;
    }
}
