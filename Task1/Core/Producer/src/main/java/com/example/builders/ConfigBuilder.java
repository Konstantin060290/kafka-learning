package com.example.builders;

import Configuration.KafkaOptions;
import org.springframework.stereotype.Component;

@Component
public class ConfigBuilder {

    public KafkaOptions GetOptions(com.example.configuration.KafkaOptions kafkaOptions)
    {
        var options = new KafkaOptions();
        options.connection.bootstrapServers = kafkaOptions.connection.bootstrapServers;

        options.producer.acks = kafkaOptions.producer.acks;
        options.producer.enableIdempotence = kafkaOptions.producer.enableIdempotence;
        options.producer.metaDataMaxAge = kafkaOptions.producer.metaDataMaxAge;
        options.producer.maxBlocksMs = kafkaOptions.producer.maxBlocksMs;
        options.producer.retries = kafkaOptions.producer.retries;
        options.producer.maxInFlightRequestsPerConnection = kafkaOptions.producer.maxInFlightRequestsPerConnection;

        return options;
    }
}
