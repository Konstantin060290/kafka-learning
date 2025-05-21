package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaOptions {
    @Autowired
    public Connection connection;
    @Autowired
    public Consumer consumer;

    @Autowired
    public Producer producer;
}