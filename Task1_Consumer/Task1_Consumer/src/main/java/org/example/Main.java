package org.example;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        // Настройка консьюмера
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.31.11:19094,192.168.31.11:29094,192.168.31.11:39094");  // Адреса брокеров Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-0");        // Уникальный идентификатор группы
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");        // Начало чтения с самого начала
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");           // Автоматический коммит смещений
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000");           // Время ожидания активности от консьюмера

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList("orders-topic"));

        // Чтение сообщений в бесконечном цикле

        try {
            while (true) {

                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));  // Получение сообщений
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n",
                                record.key(), record.value(), record.partition(), record.offset());
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        }
        finally {
            consumer.close();
        }
    }
}