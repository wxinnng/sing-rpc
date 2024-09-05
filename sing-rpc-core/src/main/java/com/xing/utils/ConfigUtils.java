package com.xing.utils;



import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.dialect.Props;
import com.xing.config.RpcConfig;
import com.xing.constant.RpcConstant;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置工具类
 */
public class ConfigUtils {

    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix);
    }


    public static void main(String[] args) {
        System.out.println(loadConfig(RpcConfig.class, "rpc", ""));
    }

    public static <T> T loadConfigFromYaml(Class<T> tClass,String prefix,String environment) {
        Yaml yaml = new Yaml();
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".yml");
        try (InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(configFileBuilder.toString())) {
            Map<String, Object> data = yaml.load(inputStream);
            T rpc = (T) BeanUtil.toBean(data.get("rpc"),tClass);
            System.out.println(rpc);
            return rpc;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }





}

