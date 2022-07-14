package com.ziyuan.controller;

import com.ziyuan.pojo.bo.TweetBO;
import com.ziyuan.service.TweetService;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TweetController extends BaseController {
    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @PostMapping("/tweet")
    public JSONResult post(@RequestBody TweetBO tweetBO,
                           HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(tweetBO.getUserId(), tweetBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }
        tweetService.postTweet(tweetBO);
        return JSONResult.ok();
    }

    @DeleteMapping("/tweet")
    public JSONResult delete(@RequestBody TweetBO tweetBO,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!userService.auth(tweetBO.getUserId(), tweetBO.getToken(), request, response)) {
            return JSONResult.errorMsg("please login again");
        }
        tweetService.deleteTweet(tweetBO.getTweetId());
        return JSONResult.ok();
    }

    @GetMapping("/tweet")
    public JSONResult get(@RequestParam String tweetId) {
        return JSONResult.ok(tweetService.getTweet(tweetId));
    }
}
