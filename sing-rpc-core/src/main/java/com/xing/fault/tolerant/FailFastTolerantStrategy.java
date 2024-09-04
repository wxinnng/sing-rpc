package com.xing.fault.tolerant;

import com.xing.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错机制
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
