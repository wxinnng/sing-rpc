package com.xing.controller;

import io.vertx.ext.web.RoutingContext;

public interface IApiController {
    void handle(RoutingContext routingContext);
}
