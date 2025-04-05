package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class KafkaOptions {
    @Autowired
    public Connection connection;
    @Autowired
    public Consumer consumer;
}