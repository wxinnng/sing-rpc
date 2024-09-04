package com.xing.provider;

import com.xing.bootstrap.ProviderBootstrap;
import com.xing.model.ServiceRegisterInfo;
import com.xing.service.UserService;
import com.xing.test.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ProviderExample2 {

    public static void main(String[] args) {
        //提供要注册的服务列表
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        //构建服务注册信息
        ServiceRegisterInfo<UserServiceImpl> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        //添加到注册列表
        serviceRegisterInfoList.add(serviceRegisterInfo);
        ProviderBootstrap.init(serviceRegisterInfoList);

    }

}
