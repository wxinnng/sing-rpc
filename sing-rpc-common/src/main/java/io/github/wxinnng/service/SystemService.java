package io.github.wxinnng.service;

import io.github.wxinnng.model.RemoteServiceInfo;
import io.github.wxinnng.model.ServiceManagementInfo;
import io.github.wxinnng.model.ServiceStrategyInfo;

import java.util.List;

public interface SystemService {
    List<RemoteServiceInfo> getServiceInfo();

    List<ServiceManagementInfo> getServiceManagementInfo();

    List<ServiceStrategyInfo> getServiceStrategyInfo();
}
