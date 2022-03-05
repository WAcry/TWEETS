package com.ziyuan.service.kafka.producer;

import com.ziyuan.pojo.ko.KafkaTest;
import com.ziyuan.utils.JsonUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

import static com.ziyuan.service.kafka.Const.KAFKA_SERVER;
import static com.ziyuan.service.kafka.Const.KAFKA_TOPIC_0;

public class MsgProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producer id"); // usually unnecessary
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // the same key (e.g. ip, user_id) goes to the same partition
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaTest kafkaTest = new KafkaTest("1", "test");
        ProducerRecord<String, String> record = new ProducerRecord<>(KAFKA_TOPIC_0, JsonUtils.objectToJson(kafkaTest));
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            producer.send(record);
            producer.send(record);
            producer.send(record);
            producer.send(record);
            producer.send(record);
            producer.send(record);
        }
    }
}
