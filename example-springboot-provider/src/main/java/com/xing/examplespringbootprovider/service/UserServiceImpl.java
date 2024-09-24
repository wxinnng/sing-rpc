package com.xing.examplespringbootprovider.service;

import com.xing.model.User;
import com.xing.service.UserService;
import com.xing.singrpcspringbootstarter.annotation.SingRpcService;
import org.springframework.stereotype.Service;

@Service
@SingRpcService(version = "4.1.0")
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        user.setName("王八羔子");
        return user;
    }
}
