package com.xing.service;

import com.xing.model.RemoteServiceInfo;
import com.xing.model.ServiceManagementInfo;

import java.util.List;

public interface SystemService {
    List<RemoteServiceInfo> getServiceInfo();
    List<ServiceManagementInfo> getServiceManagementInfo();
}
