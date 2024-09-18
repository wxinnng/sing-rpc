package com.xing.service;


import io.vertx.core.json.JsonObject;

public class SystemServiceClient {
    public static JsonObject serviceInfo(){
        SystemService instance = ServiceFactory.getInstance(SystemService.class);
        JsonObject result = new JsonObject();
        result.put("serviceInfo",instance);
        return result;
    }
}
