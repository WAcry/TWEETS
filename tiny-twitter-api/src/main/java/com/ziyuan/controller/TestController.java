package com.ziyuan.controller;

import com.ziyuan.pojo.Test;
import com.ziyuan.service.TestService;
import com.ziyuan.service.kafka.producer.SpringKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private SpringKafkaProducer springKafkaProducer;

    @PostMapping("/test")
    public void test(@RequestBody Test test) {
        testService.test(test);
    }

    @PostMapping("/send")
    public void send() {
        springKafkaProducer.send("topic_0", "key_0", "value_0");
    }
}

