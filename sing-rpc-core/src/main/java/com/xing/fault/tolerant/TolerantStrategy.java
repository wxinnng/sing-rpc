package com.xing.fault.tolerant;

import com.xing.model.RpcResponse;

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
