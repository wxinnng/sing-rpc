package com.xing.ratelimiting;

/**
 * 限流接口
 */
public interface Limiter {
    /**
     * 尝试请求
     * @return
     */
    boolean tryAcquire();
}
