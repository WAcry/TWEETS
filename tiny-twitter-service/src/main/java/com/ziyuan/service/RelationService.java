package com.ziyuan.service;


import java.util.Set;

public interface RelationService {

    public void follow(String fromUserId, String toUserId);

    public void unfollow(String fromUserId, String toUserId);

    public Set<String> getFanIds(String userId);

    public Set<String> getFollowingIds(String userId);

    public long getFollowingCount(String userId);

    public long getFanCount(String userId);
}
