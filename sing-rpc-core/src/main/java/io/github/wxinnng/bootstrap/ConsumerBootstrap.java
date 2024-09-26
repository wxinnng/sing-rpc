package io.github.wxinnng.bootstrap;

import io.github.wxinnng.RpcApplication;

/**
 * 消费者启动类
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init(){
        //RPC框架初始化[配置(序列化、重试、容错),注册中心等等]
        RpcApplication.init();
    }

}
