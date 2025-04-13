package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Stream {
    @Value("${kafka.stream.application-id}")
    public String applicationId;
    @Value("${kafka.stream.prohibited-words-topic-name}")
    public String prohibitedWordsTopicName;
    @Value("${kafka.stream.prohibited-words-store-name}")
    public String prohibitedWordsStoreName;
    @Value("${kafka.stream.blocked-users-topic-name}")
    public String blockedUsersTopicName;
    @Value("${kafka.stream.blocked-users-store-name}")
    public String blockedUsersStoreName;
    @Value("${kafka.stream.messages-topic-name}")
    public String messagesTopicName;
}
