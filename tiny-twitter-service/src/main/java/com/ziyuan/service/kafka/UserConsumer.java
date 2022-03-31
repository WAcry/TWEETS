package com.ziyuan.service.kafka;

import com.ziyuan.mapper.UserMapper;
import com.ziyuan.pojo.User;
import com.ziyuan.utils.JsonUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class UserConsumer {
    final static Logger logger = LoggerFactory.getLogger(UserConsumer.class);

    @Autowired
    private UserMapper userMapper;

    @KafkaListener(groupId = "GROUP_0", topics = "INSERT_USER")
    public void userInsert(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        User user = JsonUtils.jsonToPojo(record.value(), User.class);
        userMapper.insertSelective(user);
        logger.info("insert user: " + user);
        acknowledgment.acknowledge(); // ack
    }
}
