package io.github.wxinnng.cache.api;

import io.github.wxinnng.common.SRSMConstant;
import com.xing.controller.*;
import io.github.wxinnng.controller.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * api缓存
 */
public class ApiCache {

    //api缓存，因为这个是项目启动时加载的，而且以后都不会有修改的操作，所以不存在线程安全问题，可以直接使用HashMap
    private static final Map<String, IApiController> apiCache = new HashMap<>();

    static {
        ApiCache.addApi(SRSMConstant.API_SERVICE_INFO,new ServiceInfoController());
        ApiCache.addApi(SRSMConstant.API_SERVICE_STRATEGY,new ServiceStrategyController());
        ApiCache.addApi(SRSMConstant.API_SERVICE_GROUP,new ServiceGroupController());
        ApiCache.addApi(SRSMConstant.API_VERSION_CONTROL,new VersionControlController());
        ApiCache.addApi(SRSMConstant.API_DATA_STATISTICS,new DataStatisticsController());
        ApiCache.addApi(SRSMConstant.API_SERVICE_MANAGEMENT,new ServiceManagementController());
        ApiCache.addApi(SRSMConstant.API_LOGIN,new Login());
    }

    //添加缓存
    public static void addApi(String apiName, IApiController apiController){
        apiCache.put(apiName, apiController);
    }

    //读取缓存
    public static IApiController getApi(String apiName){
        return apiCache.get(apiName);
    }

    public static Iterator getEntryIterator(){
        return apiCache.entrySet().iterator();
    }


}
