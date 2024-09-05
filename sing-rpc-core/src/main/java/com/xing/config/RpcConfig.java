package com.xing.config;


import com.xing.fault.retry.RetryStrategy;
import com.xing.fault.retry.RetryStrategyKeys;
import com.xing.fault.tolerant.TolerantStrategyKeys;
import com.xing.loadbalancer.LoadBalancer;
import com.xing.loadbalancer.LoadBalancerKeys;
import com.xing.loadbalancer.RoundRobinLoadBalancer;
import com.xing.serializer.SerializerKeys;
import lombok.Data;

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
}