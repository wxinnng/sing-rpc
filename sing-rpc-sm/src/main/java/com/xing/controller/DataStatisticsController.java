package com.xing.controller;

import com.xing.common.SRSMConstant;
import io.vertx.ext.web.RoutingContext;

public class DataStatisticsController extends AbstractApiController implements IApiController{


    @Override
    public void handle(RoutingContext routingContext) {
        System.err.println("DataStatisticsController");
        doResponse(routingContext,"DataStatisticsController");
    }
}
