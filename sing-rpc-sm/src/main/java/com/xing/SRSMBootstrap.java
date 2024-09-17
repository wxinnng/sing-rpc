package com.xing;

import com.xing.bootstrap.ConsumerBootstrap;
import com.xing.server.RestfulAPIVerticle;
import io.vertx.core.Vertx;

import java.util.List;

public class SRSMBootstrap {
    public static void main(String[] args) {
        System.out.println("-[[\n" +
                "   _____ ____  _____ __  ___\n" +
                "  / ___// __ \\/ ___//  |/  /\n" +
                "  \\__ \\/ /_/ /\\__ \\/ /|_/ / \n" +
                " ___/ / _, _/___/ / /  / /  \n" +
                "/____/_/ |_|/____/_/  /_/   \n" +
                "                            \n" +
                "--]]");
        //服务初始化
        ConsumerBootstrap.init();

        //http服务器
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new RestfulAPIVerticle());
    }
}
