package com.xing.test;

import com.xing.model.User;
import com.xing.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("provider.UserServiceImpl.getUser");
        return user;
    }
}
