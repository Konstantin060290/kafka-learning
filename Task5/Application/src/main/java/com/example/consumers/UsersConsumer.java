package com.example.consumers;

import com.example.configuration.KafkaOptions;
import com.example.factories.KafkaConsumerFactory;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class UsersConsumer {
    @Autowired
    private KafkaOptions _kafkaOptions;
    @Autowired
    private KafkaConsumerFactory _kafkaConsumerFactory;

    @PostConstruct
    public void init() {
        new Thread(this::ConsumeUsers).start();
    }

    public void ConsumeUsers() {

        var consumer = _kafkaConsumerFactory.getConsumer(_kafkaOptions.consumer.usersTopicName);

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
        } finally {
            consumer.close();
        }
    }
}
