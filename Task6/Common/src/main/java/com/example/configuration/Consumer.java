package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Consumer {
    @Value("${kafka.consumer.group-id}")
    public String groupId;
    @Value("${kafka.consumer.auto-offset-reset}")
    public String autoOffsetReset;
    @Value("${kafka.consumer.enable-auto-commit}")
    public Boolean enableAutoCommit;
    @Value("${kafka.consumer.session-timeout}")
    public Integer sessionTimeOut;
    @Value("${kafka.consumer.topic1name}")
    public String topic1;
    @Value("${kafka.consumer.topic2name}")
    public String topic2;

    @Value("${kafka.consumer.security-protocol}")
    public String securityProtocol;

    @Value("${kafka.consumer.sasl-mechanism}")
    public String saslMechanism;

    @Value("${kafka.consumer.login}")
    public String consumerLogin;

    @Value("${kafka.consumer.password}")
    public String consumerPassword;

    @Value("${kafka.consumer.need-start-consumer2}")
    public String needStartConsumer2;
}
