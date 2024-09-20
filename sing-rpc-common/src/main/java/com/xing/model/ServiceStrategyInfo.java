package com.xing.model;


import lombok.Data;

@Data
public class ServiceStrategyInfo {
    private String host;
    private Boolean mock;
    private String loadBalancer ;
    /**
     * 默认序列化器
     */
    private String serializer;

    /**
     * 重试机制
     */
    private String retryStrategy ;
    /**
     * 容错机制
     */
    private String tolerantStrategy;
}
