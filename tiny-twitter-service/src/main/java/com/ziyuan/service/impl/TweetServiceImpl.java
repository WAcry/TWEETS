package com.ziyuan.service.impl;

import com.ziyuan.enums.YesOrNo;
import com.ziyuan.mapper.TweetMapper;
import com.ziyuan.pojo.InboxMsg;
import com.ziyuan.pojo.Tweet;
import com.ziyuan.pojo.bo.TweetBO;
import com.ziyuan.pojo.vo.TweetVO;
import com.ziyuan.pojo.vo.UserVO;
import com.ziyuan.service.*;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
public class TweetServiceImpl extends BaseService implements TweetService {

    @Autowired
    private SnowFlakeGenerator snowflake;

    @Autowired
    private RedisOperator redis;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private KafkaOperator kafka;

    @Autowired
    private RelationService relationService;

    @Autowired
    private LikeService likeService;

    @Override
    public void postTweet(TweetBO tweetBO) {
        Tweet tweet = Tweet.builder()
                .tweetId(snowflake.nextSID())
                .userId(tweetBO.getUserId())
                .content(tweetBO.getContent())
                .createdAt(new Date())
                .attachedImg(tweetBO.getAttachedImg())
                .updatedAt(new Date())
                .deleted(YesOrNo.NO.value).build();

        pushToOutbox(tweet);
        if (!redis.sismember(STARS, tweet.getUserId())) {
            pushToFansInboxes(tweet);
        }
    }

    private void pushToOutbox(Tweet tweet) {
        String tweetId = tweet.getTweetId();
        String userId = tweet.getUserId();
        redis.set(TWEET_INFO + tweetId, JsonUtils.objectToJson(tweet), TWEET_INFO_TTL);
        redis.zadd(TWEET_ID_LIST + userId, 0, tweetId); // we can sort by lex, no need score
        if (redis.zcard(TWEET_ID_LIST + userId) > TWEET_ID_LIST_MAX_COUNT) { // tweets in redis for each user is limited
            redis.zremrangebyrank(TWEET_ID_LIST + userId, 0, 0);
            redis.sadd(MORE_TWEET_ID_LIST_IN_DB, userId);
        }
        kafka.send(INSERT_TWEET, JsonUtils.objectToJson(tweet));
    }

    private void pushToFansInboxes(Tweet tweet) {
        String tweetId = tweet.getTweetId();
        String userId = tweet.getUserId();

        // we can expect fans.size() is not large, as user is not a star
        Set<String> fans = relationService.getFanIds(userId);

        for (String fanId : fans) {
            redis.zadd(INBOX_ID_LIST + fanId, 0, tweetId); // we can sort by lex, no need score
            if (redis.zcard(INBOX_ID_LIST + fanId) > INBOX_ID_LIST_MAX_COUNT) {
                redis.zremrangebyrank(INBOX_ID_LIST + fanId, 0, 0);
                redis.sadd(MORE_INBOX_ID_LIST_IN_DB, fanId);
            }

            InboxMsg inboxMsg = InboxMsg.builder()
                    .inboxMsgId(snowflake.nextSID())
                    .fromUserId(userId)
                    .toUserId(fanId)
                    .tweetId(tweetId).build();

            kafka.send(INSERT_INBOX_MSG, JsonUtils.objectToJson(inboxMsg));
        }
    }


    @Override
    public TweetVO getTweet(String tweetId) {
        String tweetJson = redis.get(TWEET_INFO + tweetId);
        Tweet tweet;
        if (StringUtils.isNotBlank(tweetJson)) {
            tweet = JsonUtils.jsonToPojo(tweetJson, Tweet.class);
            redis.expire(TWEET_INFO + tweetId, TWEET_INFO_TTL);
        } else {
            tweet = tweetMapper.selectByPrimaryKey(tweetId);
            redis.set(TWEET_INFO + tweetId, JsonUtils.objectToJson(tweet), TWEET_INFO_TTL);
        }
        return convertToTweetVO(tweet);
    }

    @Override
    public void deleteTweet(String tweetId) {
        TweetVO tweetVO = getTweet(tweetId);
        if (tweetVO == null) return;
        Tweet tweet = new Tweet();
        // clean redis tweet info
        tweet.setDeleted(YesOrNo.YES.value);
        tweet.setUpdatedAt(new Date());
        redis.set(TWEET_INFO + tweetId, JsonUtils.objectToJson(tweet), TWEET_INFO_TTL);
        // for redis inbox and tweet id list, we do lazy deletion in the future.
        kafka.send(DELETE_TWEET, JsonUtils.objectToJson(tweet));
    }

    private TweetVO convertToTweetVO(Tweet tweet) {
        if (tweet == null) return null;
        if (Objects.equals(tweet.getDeleted(), YesOrNo.YES.value)) return null;

        TweetVO tweetVO = new TweetVO();
        BeanUtils.copyProperties(tweet, tweetVO);
        UserVO userVO = userService.getUserVOById(tweet.getUserId());
        tweetVO.setUser(userVO);
        tweetVO.setLikeCount(likeService.getLikeCount(tweet.getTweetId()));
        tweetVO.setLikeStatus(likeService.isLiked(tweet.getUserId(), tweet.getTweetId()));
        return tweetVO;
    }
}
