package io.github.wxinnng.fault.tolerant;

import io.github.wxinnng.model.RpcResponse;
import io.github.wxinnng.utils.MockUtils;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        Class tClass = (Class) context.get("returnType");
        return RpcResponse.builder().data(MockUtils.getDefaultObject(tClass)).build();
    }
}
