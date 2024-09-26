package io.github.wxinnng.provider;


import io.github.wxinnng.model.User;
import io.github.wxinnng.service.UserService;

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

