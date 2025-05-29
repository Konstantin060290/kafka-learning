package com.example;

import org.springframework.beans.factory.annotation.Value;

@org.springframework.context.annotation.Configuration
public class Configuration
{
    @Value("${kafka.cluster.bootstrap-servers}")
    public String bootstrapServers;

    @Value("${kafka.scheme-registry}")
    public String schemaRegistryUrl;

    @Value("${kafka.producer-user}")
    public String producerUser;

    @Value("${kafka.producer-password}")
    public String producerPwd;

    @Value("${kafka.consumer-user}")
    public String consumerUser;

    @Value("${kafka.consumer-password}")
    public String consumerPwd;

    @Value("${kafka.truststore-location}")
    public String trustStoreLocation;

    @Value("${kafka.truststore-pwd}")
    public String trustStorePwd;

    @Value("${kafka.ca-location}")
    public String caLocation;
}
