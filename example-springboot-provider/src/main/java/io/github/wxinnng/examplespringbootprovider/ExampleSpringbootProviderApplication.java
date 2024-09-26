package io.github.wxinnng.examplespringbootprovider;

import io.github.wxinnng.singrpcspringbootstarter.annotation.EnableSingRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSingRpc
public class ExampleSpringbootProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
    }

}
