package com.xing.loadbalancer;

import com.xing.spi.SpiLoader;

/**
 * 负载均衡工厂
 */
public class LoadBalancerFactory {

    static {
        //使用SPI机制，去加载轮询算法的类
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认是轮询的负载均衡
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();


    /**
     * 拿到一个负载均衡的实例
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }

}
