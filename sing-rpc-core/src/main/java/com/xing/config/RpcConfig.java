package com.xing.config;


import com.xing.fault.retry.RetryStrategy;
import com.xing.fault.retry.RetryStrategyKeys;
import com.xing.fault.tolerant.TolerantStrategyKeys;
import com.xing.loadbalancer.LoadBalancerKeys;
import com.xing.ratelimiting.RateLimitingKeys;
import com.xing.serializer.SerializerKeys;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "sing";
    /**
     * 版本
     */
    private String version = "1.0.0";
    /**
     * 服务端地址
     */
    private String serverHost = "127.0.0.1";
    /**
     * 端口号
     */
    private Integer serverPort = 9000;

    /**
     * mock测试功能开关
     */
    private boolean mock = false;
    /**
     * 服务分组
     */
    private String serviceGroup = "default";
    /**
     * 默认序列化器
     */
    private String serializer = SerializerKeys.JDK;
    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
    /**
     * 负载均衡 (DEFAULT:ROUND_ROBIN)
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    /**
     * 重试机制
     */
    private String retryStrategy = RetryStrategyKeys.NO;
    /**
     * 容错机制
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
    /**
     * 默认没有排除
     */
    private Set<String> filterExclusionSet = new HashSet<>();
    /**
     * 是否开启请求校验
     */
    private Boolean identify = false;
    /**
     * 请求token
     */
    private String token;
    /**
     * 是否需要可视化
     */
    private Boolean srsm = false;
    /**
     * 限流策略
     */
    private String limiterKey = RateLimitingKeys.DEFAULT;
    /**
     * 最大请求数
     */
    private Integer maxRequests = RateLimitingKeys.MAX_REQUEST;
    /**
     * 时间间隔(ms)
     */
    private Long timeInterval = RateLimitingKeys.TIME_INTERVAL;
    /**
     * token限流中，最大的令牌数目
     */
    private Integer maxTokens = RateLimitingKeys.MAX_TOKENS;
}