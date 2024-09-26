package io.github.wxinnng.singrpcspringbootstarter.annotation;

import io.github.wxinnng.singrpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import io.github.wxinnng.singrpcspringbootstarter.bootstrap.RpcInitBootstrap;
import io.github.wxinnng.singrpcspringbootstarter.bootstrap.RpcProviderBootstrap;
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
public @interface EnableSingRpc {

    /**
     * 需要启动server
     *
     * @return
     */
    boolean needServer() default true;

}
