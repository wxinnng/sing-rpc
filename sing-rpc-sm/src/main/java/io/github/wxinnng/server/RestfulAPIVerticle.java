package io.github.wxinnng.server;

import io.github.wxinnng.cache.api.ApiCache;
import io.github.wxinnng.controller.IApiController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Iterator;
import java.util.Map;

public class RestfulAPIVerticle extends AbstractVerticle {

    @Override
    public void start() {
        // 创建一个 HTTP 服务器
        HttpServer server = vertx.createHttpServer();
        // 创建一个路由器
        Router router = Router.router(vertx);
        // 配置 CorsHandler
        CorsHandler corsHandler = CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.PUT)
                .allowedMethod(io.vertx.core.http.HttpMethod.DELETE)
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .maxAgeSeconds(3600);
        // 启用 BodyHandler 以处理请求体
        router.route().handler(corsHandler);

        Iterator entryIterator = ApiCache.getEntryIterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, IApiController> entry = (Map.Entry<String, IApiController>) entryIterator.next();
            String path = entry.getKey();
            IApiController controller = entry.getValue();
            router.route(path).handler(controller::handle);
        }

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


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestfulAPIVerticle());
    }
}
