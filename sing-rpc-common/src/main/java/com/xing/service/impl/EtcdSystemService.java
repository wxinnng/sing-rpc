package com.xing.service.impl;

import com.xing.model.RemoteServiceInfo;
import com.xing.model.ServiceManagementInfo;
import com.xing.service.SystemService;

import java.util.ArrayList;
import java.util.List;

public class EtcdSystemService  implements SystemService {
    @Override
    public List<RemoteServiceInfo> getServiceInfo() {
        ArrayList<RemoteServiceInfo> remoteServiceInfos = new ArrayList<>();
        RemoteServiceInfo remoteServiceInfo = new RemoteServiceInfo();
        remoteServiceInfo.setId("1");
        remoteServiceInfo.setName("userService");
        remoteServiceInfo.setUrl("http://localhost:8080");
        remoteServiceInfo.setVersion("1.0");
        remoteServiceInfo.setGroup("user");
        for(int i = 0 ;i < 9;i ++) {
            remoteServiceInfos.add(remoteServiceInfo);
        }
        return remoteServiceInfos;
    }

    @Override
    public List<ServiceManagementInfo> getServiceManagementInfo() {
        ArrayList<ServiceManagementInfo> serviceManagementInfos = new ArrayList<>();
        ServiceManagementInfo serviceManagementInfo = new ServiceManagementInfo();
        serviceManagementInfo.setServiceName("userService");
        serviceManagementInfo.setInstanceCount(12);
        serviceManagementInfo.setGroupCount(38);
        serviceManagementInfo.setVersionCount(2);
        for (int i = 0; i < 9; i++){
            serviceManagementInfos.add(serviceManagementInfo);
        }
        return serviceManagementInfos;
    }
}
