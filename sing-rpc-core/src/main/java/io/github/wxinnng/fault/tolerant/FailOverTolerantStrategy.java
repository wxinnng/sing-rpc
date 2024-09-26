package io.github.wxinnng.fault.tolerant;

import io.github.wxinnng.model.RpcResponse;

import java.util.Map;

public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo:拓展，异常转移策略
        return null;
    }
}
