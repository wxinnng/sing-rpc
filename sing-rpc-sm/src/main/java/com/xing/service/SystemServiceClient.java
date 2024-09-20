package com.xing.service;

import com.xing.cache.service.ServiceCache;
import com.xing.model.RemoteServiceInfo;
import com.xing.model.ServiceManagementInfo;
import com.xing.model.ServiceStrategyInfo;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
public class SystemServiceClient {
    public static JsonObject serviceInfo(){
        JsonObject result = new JsonObject();
        if(ServiceCache.get("serviceInfo") != null){
            log.info("通过缓存获得数据.[serviceInfo]");
            result.put("serviceInfo",(List<RemoteServiceInfo>)ServiceCache.get("serviceInfo"));
            return result;
        }
        SystemService instance = ServiceFactory.getInstance(SystemService.class);
        List<RemoteServiceInfo> serviceInfo = instance.getServiceInfo();
        ServiceCache.put("serviceInfo",serviceInfo);
        result.put("serviceInfo",serviceInfo);
        return result;
    }

    public static JsonObject serviceStrategy(){
        JsonObject result = new JsonObject();
        if(ServiceCache.get("serviceStrategy") != null){
            result.put("serviceStrategy",(List<ServiceManagementInfo>)ServiceCache.get("serviceStrategy"));
            log.info("通过缓存获得数据.[serviceStrategy]");
            return result;
        }
        //获得远程服务实例
        SystemService instance = ServiceFactory.getInstance(SystemService.class);
        log.info("通过远程服务获得数据.[serviceStrategy]");
        List<ServiceStrategyInfo> serviceStrategyInfo = instance.getServiceStrategyInfo();
        ServiceCache.put("serviceStrategy",serviceStrategyInfo);
        result.put("serviceStrategy",serviceStrategyInfo);
        return result;
    }

    public static JsonObject serviceManagement(){
        JsonObject result = new JsonObject();
        if(ServiceCache.get("serviceManagement") != null){
            result.put("serviceManagement",(List<ServiceManagementInfo>)ServiceCache.get("serviceManagement"));
            log.info("通过缓存获得数据.[serviceManagement]");
            return result;
        }
        //获得远程服务实例
        SystemService instance = ServiceFactory.getInstance(SystemService.class);
        log.info("通过远程服务获得数据. [serviceManagement]");
        List<ServiceManagementInfo> serviceManagementInfo = instance.getServiceManagementInfo();
        ServiceCache.put("serviceManagement",serviceManagementInfo);
        result.put("serviceManagement",serviceManagementInfo);
        return result;
    }
}
