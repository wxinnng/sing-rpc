package com.xing.singrpcspringbootstarter.annotation;

import com.xing.singrpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.xing.singrpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.xing.singrpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 Rpc 注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcConsumerBootstrap.class, RpcProviderBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动server
     *
     * @return
     */
    boolean needServer() default true;

}
