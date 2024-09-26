package io.github.wxinnng.config;

import io.github.wxinnng.registry.RegistryKeys;
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
     * 最多的服务数量
     */
    private Integer maxService = RegistryKeys.DEFAULT_MAX_SERVICE;

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

    /**
     * 基于最近使用频率的舍去机制
     * @return
     */
    private Boolean LRU = false;

    /**
     * 舍弃时间
     * @return
     */
    private Long expireTime = 300L;

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

