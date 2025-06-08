package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientApiOptions {
    @Value("${client-api.bootstrap-servers}")
    public String bootstrapServers;
    @Value("${client-api.producer-user}")
    public String producerUser;
    @Value("${client-api.producer-password}")
    public String producerPwd;
    @Value("${client-api.max-blocks-ms}")
    public Integer maxBlocksMs;
    @Value("${client-api.metadata-max-age}")
    public Integer metaDataMaxAge;
    @Value("${client-api.acks}")
    public String acks;
    @Value("${client-api.retries}")
    public Integer retries;
    @Value("${client-api.max-in-flight-requests-per-connection}")
    public Integer maxInFlightRequestsPerConnection;
    @Value("${client-api.producer.enable-idempotence}")
    public Boolean enableIdempotence;
    @Value("${client-api.security-protocol}")
    public String securityProtocol;
    @Value("${client-api.sasl-mechanism}")
    public String saslMechanism;
}
