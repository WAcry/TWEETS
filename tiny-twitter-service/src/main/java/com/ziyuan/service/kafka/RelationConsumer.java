package com.ziyuan.service.kafka;

import com.ziyuan.enums.YesOrNo;
import com.ziyuan.mapper.InboxMsgMapper;
import com.ziyuan.mapper.RelationMapper;
import com.ziyuan.mapper.TweetMapper;
import com.ziyuan.pojo.*;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RelationConsumer extends BaseConsumer {

    @Autowired
    private RelationMapper relationMapper;

    @Autowired
    private InboxMsgMapper inboxMsgMapper;

    @Autowired
    private SnowFlakeGenerator snowflake;

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private RedisOperator redis;

    @KafkaListener(groupId = MYSQL, topics = ADD_RELATION)
    public void addRelation(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Relation relation = JsonUtils.jsonToPojo(record.value(), Relation.class);
        relationMapper.insertSelective(relation);

        String fromUserId = relation.getFromUserId();
        String toUserId = relation.getToUserId();

        if (redis.sismember(STARS, toUserId)) return;

        TweetExample tweetExample = new TweetExample();
        tweetExample.createCriteria().andUserIdEqualTo(toUserId).andDeletedEqualTo(YesOrNo.NO.value);
        tweetMapper.selectByExample(tweetExample).forEach(tweet -> {
            InboxMsg inboxMsg = InboxMsg.builder()
                    .inboxMsgId(snowflake.nextSID())
                    .fromUserId(fromUserId)
                    .toUserId(toUserId)
                    .tweetId(tweet.getTweetId())
                    .build();
            inboxMsgMapper.insertSelective(inboxMsg);
        });

        acknowledgment.acknowledge(); // ack
    }

    @KafkaListener(groupId = MYSQL, topics = DELETE_RELATION)
    public void deleteRelation(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Relation relation = JsonUtils.jsonToPojo(record.value(), Relation.class);
        RelationExample relationExample = new RelationExample();
        relationExample.createCriteria().andFromUserIdEqualTo(relation.getFromUserId()).andToUserIdEqualTo(relation.getToUserId());
        relationMapper.deleteByExample(relationExample);

        InboxMsgExample inboxMsgExample = new InboxMsgExample();
        inboxMsgExample.createCriteria().andFromUserIdEqualTo(relation.getFromUserId()).andToUserIdEqualTo(relation.getToUserId());
        inboxMsgMapper.deleteByExample(inboxMsgExample);

        acknowledgment.acknowledge(); // ack
    }
}
