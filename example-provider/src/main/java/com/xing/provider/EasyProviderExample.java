package com.xing.provider;

import com.xing.RpcApplication;
import com.xing.registry.LocalRegistry;
import com.xing.server.HttpServer;
import com.xing.server.VertxHttpServer;
import com.xing.service.UserService;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {

    public static void main(String[] args) {

        RpcApplication.init();

        //将服务注册到本地注册中心key: serviceName(string) value: class(class)
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);


        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(9001);


    }
}
