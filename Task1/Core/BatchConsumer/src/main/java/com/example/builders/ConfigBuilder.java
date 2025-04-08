package com.example.builders;

import Configuration.KafkaOptions;
import org.springframework.stereotype.Component;

@Component
public class ConfigBuilder {

    public KafkaOptions GetOptions(com.example.configuration.KafkaOptions kafkaOptions)
    {
        KafkaOptions options = new KafkaOptions();
        options.connection.bootstrapServers = kafkaOptions.connection.bootstrapServers;

        options.consumer.autoOffsetReset = kafkaOptions.consumer.autoOffsetReset;
        options.consumer.groupId = kafkaOptions.consumer.groupId;
        options.consumer.sessionTimeOut = kafkaOptions.consumer.sessionTimeOut;
        options.consumer.enableAutoCommit = kafkaOptions.consumer.enableAutoCommit;

        return options;
    }
}
