package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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
    @Value("${kafka.consumer.topic-name}")
    public String TopicName;
}
