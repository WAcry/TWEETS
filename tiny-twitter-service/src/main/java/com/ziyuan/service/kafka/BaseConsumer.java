package com.ziyuan.service.kafka;

import com.ziyuan.enums.KafkaConsumerGroup;
import com.ziyuan.enums.KafkaTopic;
import com.ziyuan.enums.RedisPrefix;

public class BaseConsumer {

    // Kafka Topics
    public static final String INSERT_USER = KafkaTopic.INSERT_USER;
    public static final String INSERT_TWEET = KafkaTopic.INSERT_TWEET;
    public static final String INSERT_INBOX_MSG = KafkaTopic.INSERT_INBOX_MSG;
    public static final String DELETE_TWEET = KafkaTopic.DELETE_TWEET;

    public static final String ADD_RELATION = KafkaTopic.ADD_RELATION;
    public static final String DELETE_RELATION = KafkaTopic.DELETE_RELATION;

    public static final String ADD_LIKE = KafkaTopic.ADD_LIKE;
    public static final String DELETE_LIKE = KafkaTopic.DELETE_LIKE;

    // Kafka Consumer Group
    public static final String MYSQL = KafkaConsumerGroup.MYSQL;

    // Redis Prefix
    public static final String STARS = RedisPrefix.STARS.value;
    public static final String TWEET_INFO = RedisPrefix.TWEET_INFO.value;
    public static final String INBOX_ID_LIST = RedisPrefix.INBOX_ID_LIST.value;

}
