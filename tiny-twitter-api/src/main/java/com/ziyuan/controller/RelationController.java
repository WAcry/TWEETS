package com.ziyuan.controller;

import com.ziyuan.pojo.bo.RelationBO;
import com.ziyuan.service.RelationService;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RelationController extends BaseController {
    @Autowired
    private RelationService relationService;

    @Autowired
    private UserService userService;

    @PostMapping("/relation")
    public JSONResult follow(@RequestBody RelationBO relationBO,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(relationBO.getFromUserId(), relationBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        if (userService.getUserById(relationBO.getToUserId()) == null) {
            return JSONResult.errorMsg("user not exists");
        }

        if (relationService.getFollowingCount(relationBO.getFromUserId()) >= MAX_FOLLOWING_COUNT) {
            return JSONResult.errorMsg("you can follow at most " + MAX_FOLLOWING_COUNT + " users");
        }

        relationService.follow(relationBO.getFromUserId(), relationBO.getToUserId());
        return JSONResult.ok();
    }

    @DeleteMapping("/relation")
    public JSONResult unfollow(@RequestBody RelationBO relationBO,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(relationBO.getFromUserId(), relationBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        if (userService.getUserById(relationBO.getToUserId()) == null) {
            return JSONResult.errorMsg("user not exists");
        }

        relationService.unfollow(relationBO.getFromUserId(), relationBO.getToUserId());
        return JSONResult.ok();
    }


}
