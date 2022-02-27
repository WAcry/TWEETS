//package com.ziyuan.service.kafka.producer;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.Properties;
//
//public class MsgProducer {
//
//    @Value("${kafka.bootstrap.servers}")
//    private static String bootstrapServers;
//
//    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//
//        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
//            producer.send("test", "test");
//        }
//
//    }
//}
