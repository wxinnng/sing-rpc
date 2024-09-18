package com.xing.controller;

import com.xing.common.SRSMConstant;
import io.vertx.ext.web.RoutingContext;

public class ServiceStrategyController extends AbstractApiController implements IApiController{




    @Override
    public void handle(RoutingContext routingContext) {
        System.err.println("ServiceStrategyController");
        doResponse(routingContext,"ServiceStrategyController");
    }
}
