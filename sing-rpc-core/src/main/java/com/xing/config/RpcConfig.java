package com.xing.config;


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
    private Integer serverPort = 8080;

    /**
     * mock测试功能开关
     */
    private boolean mock = false;
}
