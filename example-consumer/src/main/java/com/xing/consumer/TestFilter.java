package com.xing.consumer;

import com.xing.filter.Filter;
import com.xing.filter.FilterKeys;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;

public class TestFilter implements Filter {
    @Override
    public boolean doFilter(RpcRequest rpcRequest, RpcResponse response) {
        System.err.println("我是自定义的minor");
        return true;
    }

    @Override
    public Integer getOrder() {
        return FilterKeys.DEFAULT_MINOR_FILTER_ORDER - 1;
    }

    @Override
    public Integer getType() {
        return FilterKeys.MINOR_FILTER;
    }
}
