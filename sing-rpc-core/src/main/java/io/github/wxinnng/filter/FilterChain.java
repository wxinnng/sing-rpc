package io.github.wxinnng.filter;


import io.github.wxinnng.model.RpcRequest;
import io.github.wxinnng.model.RpcResponse;

public interface FilterChain {
    void doFilter(RpcRequest request,RpcResponse response);
}
