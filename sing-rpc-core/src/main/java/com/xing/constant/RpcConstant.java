package com.xing.constant;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public interface RpcConstant {

    /**
     * 默认配置文件加载前缀
     */
    String DEFAULT_CONFIG_PREFIX = "rpc";

    /*
     * 可以读到类似于这样的配置（application.properties）
     * rpc.name=yurpc
     * rpc.version=2.0
     * rpc.serverPort=8081
     */

    String DEFAULT_SERVICE_VERSION = "1.0.0";

    String AUTHENTICATION_FAIL = "身份验证失败！";

}
