package com.xing.fault.tolerant;

import com.xing.model.RpcRequest;
import com.xing.model.RpcResponse;
import com.xing.proxy.MockServiceProxy;
import com.xing.proxy.ServiceProxyFactory;
import com.xing.utils.MockUtils;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        Class tClass = (Class) context.get("returnType");
        return RpcResponse.builder().data(MockUtils.getDefaultObject(tClass)).build();
    }
}
