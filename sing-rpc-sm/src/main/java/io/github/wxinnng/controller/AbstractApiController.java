package io.github.wxinnng.controller;

import io.github.wxinnng.cache.api.ApiCache;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class AbstractApiController {


    //添加缓存
    public static void register(String path,IApiController controller){
        ApiCache.addApi(path,controller);
    }

    protected void doResponse(RoutingContext routingContext, JsonObject jsonObject){
        // 设置响应头为 application/json
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(jsonObject.encodePrettily()); // 将 JsonObject 编码为 JSON 字符串并发送
    }
    protected void doResponse(RoutingContext routingContext, String msg){
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(msg);
    }
}
