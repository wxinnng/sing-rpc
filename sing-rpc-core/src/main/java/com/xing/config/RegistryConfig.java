package com.xing.config;

import lombok.Data;

/**
 * RPC 框架注册中心配置
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String registry = "etcd";

    /**
     * 注册中心地址
     */
    private String address ;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;

    public Long getTimeout() {
        return timeout == null? 10000L : this.timeout;
    }

    public String getRegistry() {
        return this.registry == null ? "etcd" : this.registry;
    }

    public String getAddress() {
        if(this.address != null)
            return this.address;
        if("etcd".equals(getRegistry())){
            return "http://localhost:2380";
        }else if("redis".equals(getRegistry())){
            return "http://localhost:6379";
        }else{
            return "http://localhost:2181";
        }
    }
}

