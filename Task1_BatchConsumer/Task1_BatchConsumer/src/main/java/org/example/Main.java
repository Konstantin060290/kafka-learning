package org.example;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class Main {
    public int batchSize = 10;
    public static ArrayList<String> cache = new ArrayList<String>();

    public static void main(String[] args) {

        // Настройка консьюмера
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.31.11:19094,192.168.31.11:29094,192.168.31.11:39094");  // Адрес брокера Kafka
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-1");        // Уникальный идентификатор группы
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");        // Начало чтения с самого начала

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "6000");           // Время ожидания активности от консьюмера

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList("orders-topic"));

        // Чтение сообщений в бесконечном цикле
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));  // Получение сообщений

                for (ConsumerRecord<String, String> record : records) {
                    if(cache.contains(record))
                    {
                        continue;
                    }

                    cache.add(record.value());
                }

                if(cache.size() < 10 )
                {
                    continue;
                }

                ConsumeMessage();
                consumer.commitSync();
            }
        } finally {
            consumer.close();
        }
    }

    public static void ConsumeMessage()
    {
        for (String message : cache) {
            System.out.printf("Принято сообщение: \n %s", message);
        }

        cache.clear();
    }
}