package com.xing.fault.tolerant;

import com.xing.model.RpcResponse;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo:服务降级
        return null;
    }
}
