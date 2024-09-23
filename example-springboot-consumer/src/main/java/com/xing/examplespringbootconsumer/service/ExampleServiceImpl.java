package com.xing.examplespringbootconsumer.service;


import com.xing.model.User;
import com.xing.service.UserService;
import com.xing.singrpcspringbootstarter.annotation.SingRpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {
    @SingRpcReference
    private UserService userService;
    public void test(){
        User user = new User();
        user.setName("吕圣伟");
        user = userService.getUser(user);
        System.out.println(user.getName());
    }
}
