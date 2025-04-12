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
}
