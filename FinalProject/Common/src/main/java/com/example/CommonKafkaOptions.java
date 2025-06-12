package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonKafkaOptions {
    @Value("${kafka.bootstrap-servers}")
    public String bootstrapServers;
    @Value("${kafka.security-protocol}")
    public String securityProtocol;
    @Value("${kafka.sasl-mechanism}")
    public String saslMechanism;
    @Value("${kafka.trust-store-location}")
    public String trustStoreLocation;
    @Value("${kafka.trust-store-password}")
    public String trustStorePassword;
}
