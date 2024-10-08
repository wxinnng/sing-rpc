package io.github.wxinnng.consumer;

import io.github.wxinnng.bootstrap.ConsumerBootstrap;
import io.github.wxinnng.model.User;
import io.github.wxinnng.proxy.ServiceProxyFactory;
import io.github.wxinnng.service.UserService;

public class ConsumerExample2 {

    public static void main(String[] args) throws InterruptedException {

        //服务初始化
        ConsumerBootstrap.init();

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class,null);
        User user = new User();
        user.setName("wangxing");
        for(int i = 0 ;i <= 100 ;i ++){
            System.err.println(i);
            System.out.println(userService.getUser(user).getName());
        }
    }

}
