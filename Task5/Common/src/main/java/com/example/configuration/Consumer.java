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
    @Value("${kafka.consumer.orders-topic-name}")
    public String ordersTopicName;
    @Value("${kafka.consumer.users-topic-name}")
    public String usersTopicName;
}
