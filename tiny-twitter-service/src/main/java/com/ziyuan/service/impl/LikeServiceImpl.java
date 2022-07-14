package com.ziyuan.service.impl;

import com.ziyuan.enums.YesOrNo;
import com.ziyuan.mapper.LikeMapper;
import com.ziyuan.pojo.Like;
import com.ziyuan.pojo.LikeExample;
import com.ziyuan.service.BaseService;
import com.ziyuan.service.LikeService;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl extends BaseService implements LikeService {

    @Autowired
    private SnowFlakeGenerator snowflake;


    @Autowired
    private KafkaOperator kafka;


    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private RedisOperator redis;

    @Override
    public void like(String userId, String tweetId) {
        loadLiked(userId);
        redis.sadd(LIKED + userId, tweetId);
        kafka.send(ADD_LIKE, JsonUtils.objectToJson(new Like(snowflake.nextSID(), userId, tweetId)));
    }

    @Override
    public void unlike(String userId, String tweetId) {
        loadLiked(userId);
        redis.srem(LIKED + userId, tweetId);
        kafka.send(DELETE_LIKE, JsonUtils.objectToJson(new Like(null, userId, tweetId)));
    }

    @Override
    public Integer isLiked(String userId, String tweetId) {
        loadLiked(tweetId);
        return redis.sismember(LIKED + tweetId, userId) ? YesOrNo.YES.value : YesOrNo.NO.value;
    }

    @Override
    public Long getLikeCount(String tweetId) {
        loadLiked(tweetId);
        return redis.scard(LIKED + tweetId) - 1;
    }

    private void loadLiked(String tweetId) {
        if (redis.scard(LIKED + tweetId) == 0) {
            LikeExample example = new LikeExample();
            example.createCriteria().andTweetIdEqualTo(tweetId);
            List<Like> likes = likeMapper.selectByExample(example);

            String[] userIds = new String[likes.size() + 1];
            for (int i = 1; i <= likes.size(); i++) {
                userIds[i] = likes.get(i).getUserId();
            }
            userIds[0] = "00000000"; // avoid Cache Penetration
            redis.sadd(LIKED + tweetId, userIds);
        }
        redis.expire(LIKED + tweetId, LIKED_TTL);
    }
}
