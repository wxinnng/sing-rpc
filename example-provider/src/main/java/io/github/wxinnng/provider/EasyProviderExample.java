package io.github.wxinnng.provider;

import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.registry.LocalRegistry;
import io.github.wxinnng.server.HttpServer;
import io.github.wxinnng.server.VertxHttpServer;
import io.github.wxinnng.service.UserService;

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
