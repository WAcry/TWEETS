package com.ziyuan.controller;

import com.ziyuan.pojo.Test;
import com.ziyuan.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public void test(@RequestBody Test test) {
        testService.test(test);
    }
}

