package com.xing.consumer;

import com.xing.bootstrap.ConsumerBootstrap;
import com.xing.model.User;
import com.xing.proxy.ServiceProxyFactory;
import com.xing.service.UserService;

public class ConsumerExample2 {

    public static void main(String[] args) {
        //服务初始化
        ConsumerBootstrap.init();

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("wangxing");
        User newUser = userService.getUser(user);
        if(newUser != null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("null");
        }
    }

}
