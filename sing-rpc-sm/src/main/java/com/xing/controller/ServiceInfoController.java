package com.xing.controller;


import com.xing.service.SystemServiceClient;
import io.vertx.ext.web.RoutingContext;

public class ServiceInfoController extends AbstractApiController implements IApiController{


    @Override
    public void handle(RoutingContext routingContext) {
        doResponse(routingContext,SystemServiceClient.serviceInfo());
    }
}
