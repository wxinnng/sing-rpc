package io.github.wxinnng.controller;

import io.github.wxinnng.service.SystemServiceClient;
import io.vertx.ext.web.RoutingContext;

public class ServiceStrategyController extends AbstractApiController implements IApiController{

    @Override
    public void handle(RoutingContext routingContext) {
        doResponse(routingContext, SystemServiceClient.serviceStrategy());
    }
}