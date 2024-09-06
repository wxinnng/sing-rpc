package com.xing.filter;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinorFilter implements Filter{
    @Override
    public Integer getType() {
        return FilterKeys.MINOR_FILTER;
    }

    @Override
    public boolean doFilter(RpcRequest request, RpcResponse response) {
        log.info("经过MinorFilter");
        return true;
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_MINOR_FILTER_ORDER;
    }
}
