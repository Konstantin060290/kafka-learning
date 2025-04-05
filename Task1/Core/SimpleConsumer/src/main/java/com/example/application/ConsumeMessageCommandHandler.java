package com.example.application;

import com.example.builders.ConfigBuilder;
import factories.KafkaFactory;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import shortbus.RequestHandler;

public class ConsumeMessageCommandHandler implements RequestHandler<ConsumeMessageCommand, Boolean> {

    public com.example.configuration.KafkaOptions kafkaOptions;
    private final ConfigBuilder configBuilder;
    ConsumeMessageCommandHandler(com.example.configuration.KafkaOptions kafkaOptions, ConfigBuilder configBuilder)
    {
        this.kafkaOptions = kafkaOptions;
        this.configBuilder = configBuilder;
    }
    @Override
    public Boolean handle(ConsumeMessageCommand request) {

        KafkaFactory kafkaFactory = new KafkaFactory(configBuilder.GetOptions(kafkaOptions));

        KafkaConsumer<String, String> consumer = kafkaFactory.getConsumer(kafkaOptions.consumer.TopicName);

        return true;
    }
}
