package com.ziyuan.service.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.ziyuan.service.kafka.demo.Const.KAFKA_SERVER;
import static com.ziyuan.service.kafka.demo.Const.KAFKA_TOPIC_0;

public class KafkaDemoConsumer {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_0");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
//        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(Collections.singletonList(KAFKA_TOPIC_0));
//          consumer.assign(Collections.singletonList(new TopicPartition(KAFKA_TOPIC_0, 0))); // assign to a specific partition
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    int size = partitionRecords.size();
                    String topic = partition.topic();
                    System.out.println("topic: " + topic + " partition: " + partition.partition() + "message size: " + size);
                    for (int i = 0; i < size; i++) {
                        ConsumerRecord<String, String> record = partitionRecords.get(i);
                        String key = record.key();
                        String value = record.value();
                        long offset = record.offset();
                        long commitOffset = offset + 1;
                        System.out.println("key: " + key + " value: " + value + " offset: " + offset + " commitOffset: " + commitOffset);
                        consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(commitOffset)));
                    }
                }
            }
        }
    }
}
