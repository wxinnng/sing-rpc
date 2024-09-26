package io.github.wxinnng.controller;

import io.vertx.ext.web.RoutingContext;

public class DataStatisticsController extends AbstractApiController implements IApiController{


    @Override
    public void handle(RoutingContext routingContext) {
        System.err.println("DataStatisticsController");
        doResponse(routingContext,"DataStatisticsController");
    }
}
