package com.xing.fault.tolerant;

import com.xing.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默容错机制
 * 如果出现了异常，将异常捕获，记录日志，返回空对象。
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {

        log.error("静默异常处理:{}",e.getMessage());
        //直接返回一个空对象
        return new RpcResponse();
    }
}
