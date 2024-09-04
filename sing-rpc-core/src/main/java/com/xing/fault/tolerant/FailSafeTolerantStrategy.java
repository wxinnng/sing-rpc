package com.xing.fault.tolerant;

import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默容错机制
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默异常处理:{}",e.getMessage());
        //直接返回一个空对象
        return new RpcResponse();
    }
}
