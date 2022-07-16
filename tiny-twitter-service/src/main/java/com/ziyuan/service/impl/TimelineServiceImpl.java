package com.ziyuan.service.impl;

import com.ziyuan.mapper.CustomTweetMapper;
import com.ziyuan.pojo.bo.TimelineBO;
import com.ziyuan.pojo.vo.TimelineVO;
import com.ziyuan.pojo.vo.TweetVO;
import com.ziyuan.service.BaseService;
import com.ziyuan.service.RelationService;
import com.ziyuan.service.TimelineService;
import com.ziyuan.service.TweetService;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ziyuan.utils.SnowFlakeGenerator.SID_MIN;

@Service
public class TimelineServiceImpl extends BaseService implements TimelineService {

    @Autowired
    private RedisOperator redis;

    @Autowired
    private RelationService relationService;

    @Autowired
    private CustomTweetMapper customTweetMapper;
    @Autowired
    private CustomTweetMapper customInboxMsgMapper;

    @Autowired
    private KafkaOperator kafka;

    @Autowired
    private TweetService tweetService;

    @Override
    public TimelineVO homeflow5items(TimelineBO timelineBO) {
        // homeflow is users' inbox +  tweets of stars user followed
        String userId = timelineBO.getUserId();
        String lastTweetId = timelineBO.getLastTweetId(); // last tweet we got, we want more tweets with lower id
        if (StringUtils.isBlank(lastTweetId)) { // first time to get homeflow
            lastTweetId = SnowFlakeGenerator.SID_MAX;
        }

        List<TweetVO> tweetVOs = new ArrayList<>();
        // get inbox & lazy remove deleted tweets
        while (tweetVOs.size() < 5) {
            Set<String> tweetIds = redis.zrevrangebylex(INBOX_ID_LIST + userId, SID_MIN, lastTweetId, 0, 5);

            if (tweetIds.isEmpty()) {
                if (!redis.sismember(MORE_INBOX_ID_LIST_IN_DB, userId)) break;
                tweetIds = (Set<String>) customInboxMsgMapper.select5nextTweetIds(userId, lastTweetId);
            }

            if (tweetIds.isEmpty()) break;

            for (String tweetId : tweetIds) {
                TweetVO tweetVO = tweetService.getTweet(tweetId);
                lastTweetId = tweetId;
                if (tweetVO != null) {
                    tweetVOs.add(tweetVO);
                } else { // tweet was deleted
                    redis.zrem(INBOX_ID_LIST + userId, tweetId);
                }
            }
        }

        // 2. get outbox of stars user followed
        // & lazy remove deleted tweets
        Set<String> followingIds = relationService.getFollowingIds(userId);
        for (String followingId : followingIds) {
            if (!redis.sismember(STARS, userId)) continue;
            TimelineBO followingTimelineBO = new TimelineBO(null, followingId, timelineBO.getLastTweetId());
            TimelineVO followingTimelineVO = userflow5items(followingTimelineBO);
            List<TweetVO> followingTweetVOs = followingTimelineVO.getTweets();
            tweetVOs.addAll(followingTweetVOs);
        }

        // remove items with the same tweetId
        Set<String> tweetIds = new HashSet<>();
        List<TweetVO> uniqueTweetVOs = new ArrayList<>();
        for (TweetVO tweetVO : tweetVOs) {
            if (tweetIds.contains(tweetVO.getTweetId())) continue;
            tweetIds.add(tweetVO.getTweetId());
            uniqueTweetVOs.add(tweetVO);
        }

        uniqueTweetVOs.sort((o1, o2) -> o2.getTweetId().compareTo(o1.getTweetId()));
        TimelineVO timelineVO = new TimelineVO();
        timelineVO.setTweets(uniqueTweetVOs.subList(0, Math.min(uniqueTweetVOs.size(), 5)));
        return timelineVO;
    }

    @Override
    public TimelineVO userflow5items(TimelineBO timelineBO) {
        String userId = timelineBO.getUserId();
        String lastTweetId = timelineBO.getLastTweetId(); // last tweet we got, we want more tweets with lower id
        if (StringUtils.isBlank(lastTweetId)) {
            lastTweetId = SnowFlakeGenerator.SID_MAX;
        }

        List<TweetVO> tweetVOs = new ArrayList<>();
        // get inbox & lazy remove deleted tweets
        while (tweetVOs.size() < 5) {
            Set<String> tweetIds = redis.zrevrangebylex(TWEET_ID_LIST + userId, SID_MIN, lastTweetId, 0, 5);

            if (tweetIds.isEmpty()) {
                if (!redis.sismember(MORE_TWEET_ID_LIST_IN_DB, userId)) break;
                tweetIds = (Set<String>) customTweetMapper.select5nextTweetIds(userId, lastTweetId);
            }

            if (tweetIds.isEmpty()) break;

            for (String tweetId : tweetIds) {
                TweetVO tweetVO = tweetService.getTweet(tweetId);
                lastTweetId = tweetId;
                if (tweetVO != null) {
                    tweetVOs.add(tweetVO);
                } else { // tweet was deleted
                    redis.zrem(TWEET_ID_LIST + userId, tweetId);
                }
            }
        }

        tweetVOs.sort((o1, o2) -> o2.getTweetId().compareTo(o1.getTweetId()));
        TimelineVO timelineVO = new TimelineVO();
        timelineVO.setTweets(tweetVOs.subList(0, Math.min(tweetVOs.size(), 5)));
        return timelineVO;
    }
}
