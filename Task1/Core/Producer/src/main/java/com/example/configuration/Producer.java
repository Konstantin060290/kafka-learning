package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Producer {
    @Value("${kafka.producer.max-blocks-ms}")
    public Integer maxBlocksMs;
    @Value("${kafka.producer.metadata-max-age-config}")
    public Integer metaDataMaxAge;
    @Value("${kafka.producer.acks}")
    public String acks;
    @Value("${kafka.producer.retries}")
    public Integer retries;
    @Value("${kafka.producer.max-in-flight-requests-per-connection}")
    public Integer maxInFlightRequestsPerConnection;
    @Value("${kafka.producer.enable-idempotence}")
    public Boolean enableIdempotence;
}
