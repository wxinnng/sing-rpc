package io.github.wxinnng.examplespringbootconsumer;

import io.github.wxinnng.singrpcspringbootstarter.annotation.EnableSingRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//消费端不需要开启服务器
@EnableSingRpc(needServer = false)
public class ExampleSpringbootConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootConsumerApplication.class, args);
    }
}
