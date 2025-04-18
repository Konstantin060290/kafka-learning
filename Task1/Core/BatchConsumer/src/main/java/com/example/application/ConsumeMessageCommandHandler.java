package com.example.application;

import com.example.builders.ConfigBuilder;
import factories.KafkaConsumerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import shortbus.RequestHandler;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

@Component
public class ConsumeMessageCommandHandler implements RequestHandler<ConsumeMessageCommand, Void> {

    public com.example.configuration.KafkaOptions kafkaOptions;
    private final ConfigBuilder configBuilder;
    public static HashSet<String> cache = new HashSet();
    ConsumeMessageCommandHandler(com.example.configuration.KafkaOptions kafkaOptions, ConfigBuilder configBuilder)
    {
        this.kafkaOptions = kafkaOptions;
        this.configBuilder = configBuilder;
    }
    @Override
    public Void handle(ConsumeMessageCommand request) {

        KafkaConsumerFactory kafkaFactory = new KafkaConsumerFactory(configBuilder.GetOptions(kafkaOptions));

        KafkaConsumer<String, String> consumer = kafkaFactory.getConsumer(kafkaOptions.consumer.TopicName);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList(kafkaOptions.consumer.TopicName));

        // Чтение сообщений в бесконечном цикле
        try {
            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));  // Получение сообщений

                    for (ConsumerRecord<String, String> record : records) {
                        cache.add(record.value());
                    }

                    if (cache.size() < kafkaOptions.consumer.MinBatchSize) {
                        continue;
                    }

                    ConsumeMessage();
                    consumer.commitSync();
                }
                catch (Exception e) {
                    System.out.print(e);
                }
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
