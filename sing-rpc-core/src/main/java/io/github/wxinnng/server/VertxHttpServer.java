package io.github.wxinnng.server;


import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertx HTTP 服务器
 */
@Slf4j
public class VertxHttpServer implements HttpServer {

    /**
     * 启动服务器
     *
     * @param port
     */
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动 HTTP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("Server is now listening on port " + port);
            } else {
                log.error("Failed to start server: " + result.cause());
            }
        });
    }
}

