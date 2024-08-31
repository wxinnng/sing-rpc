package com.xing.consumer;


import com.xing.RpcApplication;
import com.xing.config.RpcConfig;
import com.xing.model.User;
import com.xing.proxy.ServiceProxyFactory;
import com.xing.service.UserService;
import com.xing.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {
        RpcApplication.init();
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("xing");
        System.out.println(userService.getUser(user));

        System.out.println(userService.getNumber());
    }
}
