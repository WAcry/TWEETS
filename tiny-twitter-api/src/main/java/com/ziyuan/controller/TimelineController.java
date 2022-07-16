package com.ziyuan.controller;

import com.ziyuan.pojo.bo.TimelineBO;
import com.ziyuan.pojo.vo.TimelineVO;
import com.ziyuan.service.TimelineService;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("timeline")
public class TimelineController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private TimelineService timelineService;


    @GetMapping("homeflow")
    public JSONResult homeflow(@RequestBody TimelineBO timelineBO,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(timelineBO.getUserId(), timelineBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        TimelineVO timelineVO = timelineService.homeflow5items(timelineBO);

        return JSONResult.ok(timelineVO);
    }

    @GetMapping("userflow")
    public JSONResult userflow(@RequestBody TimelineBO timelineBO,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(timelineBO.getUserId(), timelineBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        TimelineVO timelineVO = timelineService.userflow5items(timelineBO);

        return JSONResult.ok(timelineVO);
    }
}
