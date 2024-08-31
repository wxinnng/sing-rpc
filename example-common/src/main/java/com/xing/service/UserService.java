package com.xing.service;


import com.xing.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * 新方法 - 获取数字
     * @return
     */
    default short getNumber(){
        return 1;
    }
}
