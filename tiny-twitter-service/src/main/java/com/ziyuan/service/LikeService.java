package com.ziyuan.service;


public interface LikeService {

    public void like(String userId, String tweetId);

    public void unlike(String userId, String tweetId);

    public Integer isLiked(String userId, String tweetId);

    public Long getLikeCount(String tweetId);
}
