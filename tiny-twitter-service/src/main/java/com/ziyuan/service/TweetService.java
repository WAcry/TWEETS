package com.ziyuan.service;

import com.ziyuan.pojo.bo.TweetBO;
import com.ziyuan.pojo.vo.TweetVO;

public interface TweetService {
    public void postTweet(TweetBO tweetBO);

    public TweetVO getTweet(String id);

    public void deleteTweet(String id);
}
