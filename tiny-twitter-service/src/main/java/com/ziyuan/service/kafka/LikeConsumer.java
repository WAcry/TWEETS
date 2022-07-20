package com.ziyuan.service.kafka;

import com.ziyuan.mapper.LikeMapper;
import com.ziyuan.pojo.Like;
import com.ziyuan.pojo.LikeExample;
import com.ziyuan.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LikeConsumer extends BaseConsumer {

    @Autowired
    private LikeMapper likeMapper;

    @KafkaListener(groupId = MYSQL, topics = ADD_LIKE)
    public void addLike(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Like like = JsonUtils.jsonToPojo(record.value(), Like.class);
        likeMapper.insertSelective(like);
        acknowledgment.acknowledge(); // ack
    }

    @KafkaListener(groupId = MYSQL, topics = DELETE_LIKE)
    public void deleteLike(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Like like = JsonUtils.jsonToPojo(record.value(), Like.class);
        LikeExample example = new LikeExample();
        example.createCriteria().andUserIdEqualTo(like.getUserId()).andTweetIdEqualTo(like.getTweetId());
        likeMapper.deleteByExample(example);
        acknowledgment.acknowledge(); // ack
    }
}
