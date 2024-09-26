package io.github.wxinnng.provider;

import io.github.wxinnng.bootstrap.ProviderBootstrap;
import io.github.wxinnng.model.ServiceRegisterInfo;
import io.github.wxinnng.service.UserService;


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
        //初始化服务提供者
        ProviderBootstrap.init(serviceRegisterInfoList);
    }

}
