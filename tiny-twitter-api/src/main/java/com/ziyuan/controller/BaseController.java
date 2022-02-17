package com.ziyuan.controller;

import com.ziyuan.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BaseController {

    @Autowired
    private RedisOperator redis;

    public boolean auth(String usrId, String token) {
        if (StringUtils.isNotBlank(usrId) && StringUtils.isNotBlank(token)) {
            String realToken = redis.get("token:" + usrId);
            return !StringUtils.isBlank(realToken) && realToken.equals(token);
        }
        return false;
    }
}
