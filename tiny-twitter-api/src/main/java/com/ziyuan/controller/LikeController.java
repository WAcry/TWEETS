package com.ziyuan.controller;

import com.ziyuan.pojo.bo.LikeBO;
import com.ziyuan.service.LikeService;
import com.ziyuan.service.TweetService;
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
public class LikeController extends BaseController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;


    @PostMapping("/like")
    public JSONResult like(@RequestBody LikeBO likeBO,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(likeBO.getUserId(), likeBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        if (tweetService.getTweet(likeBO.getTweetId()) == null) {
            return JSONResult.ok();
        }

        likeService.like(likeBO.getUserId(), likeBO.getTweetId());
        return JSONResult.ok();
    }

    @DeleteMapping("/unlike")
    public JSONResult unlike(@RequestBody LikeBO likeBO,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(likeBO.getUserId(), likeBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }

        if (tweetService.getTweet(likeBO.getTweetId()) == null) {
            return JSONResult.ok();
        }

        likeService.unlike(likeBO.getUserId(), likeBO.getTweetId());
        return JSONResult.ok();
    }
}
