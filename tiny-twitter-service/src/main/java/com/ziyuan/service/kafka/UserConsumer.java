package com.ziyuan.service.kafka;

import com.ziyuan.mapper.UserMapper;
import com.ziyuan.pojo.User;
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
public class UserConsumer extends BaseConsumer {
    @Autowired
    private UserMapper userMapper;

    @KafkaListener(groupId = MYSQL, topics = INSERT_USER)
    public void insertUser(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        User user = JsonUtils.jsonToPojo(record.value(), User.class);
        userMapper.insertSelective(user);
        log.info("insert user: " + user);
        acknowledgment.acknowledge(); // ack
    }
}
