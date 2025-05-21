package com.example;

import com.example.configuration.KafkaOptions;
import com.example.factories.KafkaConsumerFactory;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumer {
    @Autowired
    private KafkaOptions _kafkaOptions;
    @Autowired
    private KafkaConsumerFactory _kafkaConsumerFactory;

    public SimpleConsumer() {
    }

    @PostConstruct
    public void init() {
        (new Thread(this::ConsumeUsers)).start();
    }

    public void ConsumeUsers() {
        KafkaConsumer<String, String> consumer = this._kafkaConsumerFactory.getConsumer(this._kafkaOptions.consumer.topic1);

        try {
            while(true) {
                try {
                    for(ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {
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