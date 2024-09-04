package com.xing.fault.retry;

import com.xing.model.RpcResponse;

import java.util.concurrent.Callable;

public interface RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
