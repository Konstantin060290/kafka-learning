package com.example.application;

import com.example.builders.ConfigBuilder;
import factories.KafkaFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import shortbus.RequestHandler;

import java.time.Duration;
import java.util.Collections;

@Component
public class ConsumeMessageCommandHandler implements RequestHandler<ConsumeMessageCommand, Void> {

    public com.example.configuration.KafkaOptions kafkaOptions;
    private final ConfigBuilder configBuilder;
    ConsumeMessageCommandHandler(com.example.configuration.KafkaOptions kafkaOptions, ConfigBuilder configBuilder)
    {
        this.kafkaOptions = kafkaOptions;
        this.configBuilder = configBuilder;
    }
    @Override
    public Void handle(ConsumeMessageCommand request) {

        KafkaFactory kafkaFactory = new KafkaFactory(configBuilder.GetOptions(kafkaOptions));

        KafkaConsumer<String, String> consumer = kafkaFactory.getConsumer(kafkaOptions.consumer.TopicName);

        // Подписка на топик
        consumer.subscribe(Collections.singletonList(kafkaOptions.consumer.TopicName));

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
