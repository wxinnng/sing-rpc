package com.xing.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;


import java.util.HashMap;
import java.util.Map;

public class RestfulAPIVerticle extends AbstractVerticle {

    private Map<String, JsonObject> products = new HashMap<>();

    @Override
    public void start() {
        // 创建一个 HTTP 服务器
        HttpServer server = vertx.createHttpServer();

        // 创建一个路由器
        Router router = Router.router(vertx);

        // 启用 BodyHandler 以处理请求体
        router.route().handler(BodyHandler.create());

        // 定义 RESTful API 路由
        router.get("/api/products/:productID").handler(this::handleGetProduct);

        router.route("/srsm/*").handler(StaticHandler.create().setCachingEnabled(false));
        // 将路由器与服务器关联
        server.requestHandler(router).listen(9162, http -> {
            if (http.succeeded()) {
                System.out.println("HTTP server started on port 9162");
            } else {
                System.err.println("Failed to start HTTP server");
            }
        });
    }

    private void handleGetProduct(RoutingContext routingContext) {
        System.err.println("Resolution!!!!!!!!");
        String productID = routingContext.request().getParam("productID");
        JsonObject product = products.get(productID);

        if (product == null) {
            routingContext.response()
                    .setStatusCode(404)
                    .putHeader("content-type", "application/json")
                    .end(new JsonObject().put("message", "Product not found").encodePrettily());
        } else {
            routingContext.response()
                    .putHeader("content-type", "application/json")
                    .end(product.encodePrettily());
        }
    }


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestfulAPIVerticle());
    }
}
