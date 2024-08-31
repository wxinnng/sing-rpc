package com.xing;

import com.xing.config.RpcConfig;
import com.xing.constant.RpcConstant;
import com.xing.utils.ConfigUtils;

public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 通过自定义配置初始化
     * @param rpcConfig
     */
    public static void init(RpcConfig rpcConfig){
        RpcApplication.rpcConfig = rpcConfig;
//        log.info("rpc init,config = {}",rpcConfig);
    }


    public static void init(){
        RpcConfig newRpcConfig;
        try{
            //加载配置文件到rpcConfig
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch(Exception e){
            //配置加载失败，使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 单例模式，保证全局配置唯一（懒汉 + 双重检验锁）
     * @return
     */
    public static RpcConfig getRpcConfig(){
        if(rpcConfig == null){
            synchronized (RpcApplication.class){
                if(rpcConfig == null){
                    rpcConfig = new RpcConfig();
                }
            }
        }
        return rpcConfig;
    }
}
