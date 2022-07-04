package com.ziyuan.controller;

import com.ziyuan.enums.Limits;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    final static int MAX_FOLLOWING_COUNT = Limits.MAX_FOLLOWING_COUNT.value;
}
