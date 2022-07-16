package com.ziyuan.service.impl;

import com.ziyuan.mapper.RelationMapper;
import com.ziyuan.pojo.Relation;
import com.ziyuan.pojo.RelationExample;
import com.ziyuan.pojo.bo.TimelineBO;
import com.ziyuan.pojo.vo.TimelineVO;
import com.ziyuan.pojo.vo.TweetVO;
import com.ziyuan.service.BaseService;
import com.ziyuan.service.RelationService;
import com.ziyuan.service.TimelineService;
import com.ziyuan.service.TweetService;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RelationServiceImpl extends BaseService implements RelationService {

    @Autowired
    private SnowFlakeGenerator snowflake;

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private KafkaOperator kafka;

    @Autowired
    private TimelineService timelineService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private RedisOperator redis;

    @Override
    public void follow(String fromUserId, String toUserId) {
        addRelationInRedis(fromUserId, toUserId);
        pullInboxInRedis(fromUserId, toUserId);

        // send message to kafka and update in DB
        Relation relation = Relation.builder()
                .relationId(snowflake.nextSID())
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .build();
        kafka.send(ADD_RELATION, JsonUtils.objectToJson(relation));
    }

    private void addRelationInRedis(String fromUserId, String toUserId) {
        loadFanIds(toUserId); // load to redis if needed and refresh ttl
        redis.sadd(FANS + toUserId, fromUserId);
        if (getFanCount(toUserId) >= STAR_THRESHOLD) {
            redis.sadd(STARS, toUserId);
        }
        loadFollowingIds(fromUserId); // load to redis if needed and refresh ttl
        redis.sadd(FOLLOWING + fromUserId, toUserId);
    }

    private void pullInboxInRedis(String fromUserId, String toUserId) {
        String lastTweetId = null;
        for (int i = 0; i < INBOX_ID_LIST_MAX_COUNT; i += 5) {
            TimelineVO timelineVO = timelineService.userflow5items(new TimelineBO("", toUserId, lastTweetId));
            for (TweetVO tweetVO : timelineVO.getTweets()) {
                redis.zadd(INBOX_ID_LIST + fromUserId, 0, tweetVO.getTweetId());
                lastTweetId = tweetVO.getTweetId();
            }
        }
        long size = redis.zcard(INBOX_ID_LIST + fromUserId);
        if (size > INBOX_ID_LIST_MAX_COUNT) {
            redis.zremrangebyrank(INBOX_ID_LIST + fromUserId, 0, size - INBOX_ID_LIST_MAX_COUNT - 1);
            redis.sadd(MORE_INBOX_ID_LIST_IN_DB, fromUserId);
        }
    }

    @Override
    public void unfollow(String fromUserId, String toUserId) {
        removeRelationInRedis(fromUserId, toUserId);
        removeInboxMsgInRedis(fromUserId, toUserId);

        // send message to kafka and update in DB
        Relation relation = Relation.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .build();
        kafka.send(DELETE_RELATION, JsonUtils.objectToJson(relation));
    }

    private void removeRelationInRedis(String fromUserId, String toUserId) {
        // update following & follow
        loadFanIds(toUserId);
        redis.srem(FANS + toUserId, fromUserId);

        loadFollowingIds(fromUserId);
        redis.srem(FOLLOWING + fromUserId, toUserId);
    }

    private void removeInboxMsgInRedis(String fromUserId, String toUserId) {
        Set<String> tweetIds = redis.zrange(INBOX_ID_LIST + fromUserId, 0, -1);
        List<String> tweetsIdNeedToRemove = new ArrayList<>();
        for (String tweetId : tweetIds) {
            TweetVO tweetVO = tweetService.getTweet(tweetId);
            if (tweetVO.getUser().getUserId().equals(toUserId)) {
                tweetsIdNeedToRemove.add(tweetId);
            }
        }
        redis.zrem(INBOX_ID_LIST + fromUserId, tweetsIdNeedToRemove.toArray(new String[0]));
    }

    @Override
    public Set<String> getFanIds(String userId) {
        loadFanIds(userId);
        Set<String> fans = redis.smembers(FANS + userId);
        fans.remove(userId);
        return fans;
    }

    private void loadFanIds(String userId) {
        long fanCount = redis.scard(FANS + userId);
        if (fanCount == 0) {
            RelationExample example = new RelationExample();
            example.createCriteria().andToUserIdEqualTo(userId);
            List<Relation> relations = relationMapper.selectByExample(example);
            Set<String> fanIds = new HashSet<>();
            for (Relation relation : relations) {
                fanIds.add(relation.getFromUserId());
            }
            fanIds.add(userId); // add self to fans but not return, avoid cache penetration
            redis.sadd(FANS + userId, fanIds.toArray(new String[0]));
        }
        redis.expire(FANS + userId, FANS_TTL);
    }

    @Override
    public Set<String> getFollowingIds(String userId) {
        loadFollowingIds(userId);
        Set<String> following = redis.smembers(FOLLOWING + userId);
        following.remove(userId);
        return following;
    }

    private void loadFollowingIds(String userId) {
        long followingCount = redis.scard(FOLLOWING + userId);
        if (followingCount == 0) {
            RelationExample example = new RelationExample();
            example.createCriteria().andFromUserIdEqualTo(userId);
            List<Relation> relations = relationMapper.selectByExample(example);
            Set<String> followingIds = new HashSet<>();
            for (Relation relation : relations) {
                followingIds.add(relation.getToUserId());
            }
            followingIds.add(userId); // add self to following but not return, avoid cache penetration
            redis.sadd(FOLLOWING + userId, followingIds.toArray(new String[0]));
        }
        redis.expire(FOLLOWING + userId, FOLLOWING_TTL);
    }

    @Override
    public long getFollowingCount(String userId) {
        return redis.scard(FOLLOWING + userId);
    }

    @Override
    public long getFanCount(String userId) {
        return redis.scard(FANS + userId);
    }
}
