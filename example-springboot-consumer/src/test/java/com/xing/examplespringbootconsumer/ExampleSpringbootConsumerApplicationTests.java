package com.xing.examplespringbootconsumer;

import com.xing.examplespringbootconsumer.service.ExampleServiceImpl;
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
