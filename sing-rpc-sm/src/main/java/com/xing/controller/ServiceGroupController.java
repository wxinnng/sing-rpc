package com.xing.controller;

import com.xing.common.SRSMConstant;
import io.vertx.ext.web.RoutingContext;

public class ServiceGroupController extends AbstractApiController implements IApiController{

    static {
        register(SRSMConstant.API_SERVICE_GROUP,new ServiceGroupController());
    }

    @Override
    public void handle(RoutingContext routingContext) {
        //处理请求
        System.err.println("ServiceGroupController");
        doResponse(routingContext,"ServiceGroupController");
    }
}
