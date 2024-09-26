package io.github.wxinnng.examplespringbootprovider.service;

import io.github.wxinnng.model.User;
import io.github.wxinnng.service.UserService;
import io.github.wxinnng.singrpcspringbootstarter.annotation.SingRpcService;
import org.springframework.stereotype.Service;

@Service
@SingRpcService(version = "4.1.0",group = "shanghai")
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        user.setName("王八羔子");
        return user;
    }
}
