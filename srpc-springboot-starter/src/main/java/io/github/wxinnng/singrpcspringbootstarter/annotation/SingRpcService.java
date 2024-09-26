package io.github.wxinnng.singrpcspringbootstarter.annotation;

import io.github.wxinnng.constant.RpcConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingRpcService {

    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     * @return
     */
    String version() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 服务分组
     * @return
     */
    String group() default RpcConstant.DEFAULT_GROUP;

}
