package com.xing.provider;


import com.xing.model.User;
import com.xing.service.UserService;

/**
 * 用户服务实现类
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("provider.UserServiceImpl.getUser");
        return user;
    }
}

