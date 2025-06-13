package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientApiOptions {
    @Value("${client-api.producer-user:producer}")
    public String producerUser;
    @Value("${client-api.producer-password:producer-secret}")
    public String producerPwd;
    @Value("${client-api.max-blocks-ms:5000}")
    public Integer maxBlocksMs;
    @Value("${client-api.metadata-max-age:5000}")
    public Integer metaDataMaxAge;
    @Value("${client-api.acks:all}")
    public String acks;
    @Value("${client-api.retries:2147483647}")
    public Integer retries;
    @Value("${client-api.max-in-flight-requests-per-connection:1}")
    public Integer maxInFlightRequestsPerConnection;
    @Value("${client-api.producer.enable-idempotence:true}")
    public Boolean enableIdempotence;
}
