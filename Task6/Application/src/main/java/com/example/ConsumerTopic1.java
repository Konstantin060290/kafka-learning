package com.example;

import com.example.configuration.KafkaOptions;
import com.example.factories.KafkaFactory;
import jakarta.annotation.PostConstruct;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerTopic1 {
    @Autowired
    private KafkaOptions _kafkaOptions;
    @Autowired
    private KafkaFactory _kafkaFactory;

    public ConsumerTopic1() {
    }

    @PostConstruct
    public void init() {
        (new Thread(this::ConsumeUsers)).start();
    }

    public void ConsumeUsers() {
        KafkaConsumer<String, String> consumer = this._kafkaFactory.getConsumer(this._kafkaOptions.consumer.topic1);

        try {
            while(true) {
                try {
                    for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {
                        System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n", record.key(), record.value(), record.partition(), record.offset());
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        } finally {
            consumer.close();
        }
    }
}