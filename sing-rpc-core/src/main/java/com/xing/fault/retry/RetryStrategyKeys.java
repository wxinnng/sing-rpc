package com.xing.fault.retry;

public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";
    /**
     * 固定时长
     */
    String FIXED_INTERVAL = "fixedInterval";


}
