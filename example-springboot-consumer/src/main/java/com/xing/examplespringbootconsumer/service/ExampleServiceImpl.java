package com.xing.examplespringbootconsumer.service;


import com.xing.model.User;
import com.xing.service.UserService;
import com.xing.singrpcspringbootstarter.annotation.SingRpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @SingRpcReference(version = "4.1.0",group = "shanghai")
    private UserService userService;
    public void test(){
        User user = new User();
        user = userService.getUser(user);
        System.out.println("name is " + user.getName());
    }
}
