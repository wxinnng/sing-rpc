package com.xing.filter;

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
    boolean doFilter(RpcRequest rpcRequest, RpcResponse response);

    /**
     * 排序
     * @return
     */
    Integer getOrder();

    /**
     * 类型
     */
    Integer getType();
}
