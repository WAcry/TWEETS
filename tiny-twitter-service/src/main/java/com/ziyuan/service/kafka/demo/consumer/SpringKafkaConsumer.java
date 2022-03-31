package com.ziyuan.service.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class SpringKafkaConsumer {
    final static Logger logger = LoggerFactory.getLogger(SpringKafkaConsumer.class);

    @KafkaListener(groupId = "group_0", topics = "topic_0")
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        logger.info("received message: " + record.value());
        acknowledgment.acknowledge(); // ack
    }
}
