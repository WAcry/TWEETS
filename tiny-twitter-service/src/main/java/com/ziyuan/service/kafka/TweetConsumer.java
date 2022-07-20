package com.ziyuan.service.kafka;

import com.ziyuan.mapper.InboxMsgMapper;
import com.ziyuan.mapper.TweetMapper;
import com.ziyuan.pojo.InboxMsg;
import com.ziyuan.pojo.Tweet;
import com.ziyuan.utils.JsonUtils;
import com.ziyuan.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TweetConsumer extends BaseConsumer {

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private InboxMsgMapper inboxMsgMapper;

    @Autowired
    private RedisOperator redis;

    @KafkaListener(groupId = MYSQL, topics = INSERT_TWEET)
    public void insertTweet(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Tweet tweet = JsonUtils.jsonToPojo(record.value(), Tweet.class);

        tweetMapper.insertSelective(tweet);
        log.info("insert tweet: " + tweet);

        acknowledgment.acknowledge(); // ack
    }

    @KafkaListener(groupId = MYSQL, topics = DELETE_TWEET)
    public void deleteTweet(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        Tweet tweet = JsonUtils.jsonToPojo(record.value(), Tweet.class);

        tweetMapper.updateByPrimaryKeySelective(tweet);
        log.info("delete tweet: " + tweet);

        acknowledgment.acknowledge(); // ack
    }

    @KafkaListener(groupId = MYSQL, topics = INSERT_INBOX_MSG)
    public void insertInboxMsg(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        InboxMsg inboxMsg = JsonUtils.jsonToPojo(record.value(), InboxMsg.class);

        inboxMsgMapper.insertSelective(inboxMsg);
        log.info("insert inboxMsg: " + inboxMsg);

        acknowledgment.acknowledge(); // ack
    }
}
