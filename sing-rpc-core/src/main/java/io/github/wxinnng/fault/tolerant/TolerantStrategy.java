package io.github.wxinnng.fault.tolerant;

import io.github.wxinnng.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {


    /**
     *
     * @param context
     * @param e
     * @return
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e);

}
