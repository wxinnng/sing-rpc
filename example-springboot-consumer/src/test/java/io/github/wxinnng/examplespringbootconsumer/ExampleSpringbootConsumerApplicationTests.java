package io.github.wxinnng.examplespringbootconsumer;

import io.github.wxinnng.examplespringbootconsumer.service.ExampleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExampleSpringbootConsumerApplicationTests {
    @Autowired
    private ExampleServiceImpl exampleService;
    @Test
    void contextLoads() {
        exampleService.test();
    }

}
