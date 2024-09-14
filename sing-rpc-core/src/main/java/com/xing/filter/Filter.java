package com.xing.filter;

import com.xing.exception.RequestRejectException;
import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;

/**
 * 过滤器的顶层接口
 */
public interface Filter {



    /**
     * 过滤功能
     * @return
     */
    void doFilter(RpcRequest rpcRequest, RpcResponse response,FilterChain filterChain) throws RequestRejectException;

    /**
     * 排序
     * @return
     */
    Integer getOrder();

    /**
     * 类型
     */
    Integer getType();

    /**
     * 前置处理器
     * @param rpcRequest
     * @param rpcResponse
     */
    default void Preprocessing(RpcRequest rpcRequest,RpcResponse rpcResponse){

    }

    /**
     * 后置处理器
     * @param rpcRequest
     * @param rpcResponse
     */
    default  void Postprocessing(RpcRequest rpcRequest,RpcResponse rpcResponse){

    }
}
