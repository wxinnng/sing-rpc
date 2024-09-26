package io.github.wxinnng.examplespringbootconsumer.service;


import io.github.wxinnng.model.User;
import io.github.wxinnng.service.UserService;
import io.github.wxinnng.singrpcspringbootstarter.annotation.SingRpcReference;
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
