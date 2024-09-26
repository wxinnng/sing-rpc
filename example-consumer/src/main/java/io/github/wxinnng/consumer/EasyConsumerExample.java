package io.github.wxinnng.consumer;


import io.github.wxinnng.RpcApplication;
import io.github.wxinnng.model.User;
import io.github.wxinnng.proxy.ServiceProxyFactory;
import io.github.wxinnng.service.UserService;

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
