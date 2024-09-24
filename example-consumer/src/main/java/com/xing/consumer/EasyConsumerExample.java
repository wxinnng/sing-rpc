package com.xing.consumer;


import com.xing.RpcApplication;
import com.xing.model.User;
import com.xing.proxy.ServiceProxyFactory;
import com.xing.service.UserService;

/**
 * 简易服务消费者示例
 */
public class EasyConsumerExample {

    public static void main(String[] args) {

        RpcApplication.init();

        UserService userService = ServiceProxyFactory.getProxy(UserService.class,null);

        User user = new User();
        user.setName("xing");

        System.out.println(userService.getUser(user).getName());

        System.out.println(userService.getNumber());
    }
}
