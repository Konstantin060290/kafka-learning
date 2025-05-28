package com.example;

import com.example.configuration.KafkaOptions;
import com.example.factories.KafkaFactory;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ConsumerTopic2 {
    @Autowired
    private KafkaOptions _kafkaOptions;
    @Autowired
    private KafkaFactory _kafkaFactory;

    public ConsumerTopic2() {
    }

    @PostConstruct
    public void init() {
        (new Thread(this::ConsumeUsers)).start();
    }

    public void ConsumeUsers() {

        if(Boolean.parseBoolean(_kafkaOptions.consumer.needStartConsumer2) == false)
        {
            return;
        }

        KafkaConsumer<String, String> consumer = this._kafkaFactory.getConsumer(this._kafkaOptions.consumer.topic2);

        try {
                try {
                    for(ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100L))) {
                        System.out.printf("Получено сообщение: key = %s, value = %s, partition = %d, offset = %d%n", record.key(), record.value(), record.partition(), record.offset());
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }
        } finally {
            consumer.close();
        }
    }
}