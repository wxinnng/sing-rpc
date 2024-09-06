package com.xing;

import com.xing.config.RegistryConfig;
import com.xing.config.RpcConfig;
import com.xing.constant.RpcConstant;
import com.xing.registry.Registry;
import com.xing.registry.RegistryFactory;
import com.xing.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;


    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);

        //创建并注册JVM Shutdown Hook ,JVM退出时，操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));

    }



    public static void init(){
        RpcConfig newRpcConfig = null;
        try{
            //加载配置文件到rpcConfig ,properties文件
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch(Exception e){
            try{
                //yml文件
                newRpcConfig = ConfigUtils.loadConfigFromYaml(RpcConfig.class,RpcConstant.DEFAULT_CONFIG_PREFIX,"");
            } catch (Exception ex) {
                //配置加载失败，使用默认配置
                newRpcConfig = new RpcConfig();
            }
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
