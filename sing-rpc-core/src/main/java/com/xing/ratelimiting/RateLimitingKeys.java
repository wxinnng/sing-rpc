package com.xing.ratelimiting;

public interface RateLimitingKeys {

    /**
     * 默认最大请求数，100
     */
    Integer MAX_REQUEST = 100;
    /**
     * 默认请求时间间隔 1s
     */
    Long TIME_INTERVAL = 1000L;
    /**
     * 默认限流器类型，滑动窗口
     */
    String TYPE = "slidingWindowRateLimiter";
}
