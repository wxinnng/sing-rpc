package io.github.wxinnng.controller;

import io.vertx.ext.web.RoutingContext;

public interface IApiController {
    void handle(RoutingContext routingContext);
}
