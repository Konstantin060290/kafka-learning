package com.example.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shortbus.Mediator;
import shortbus.MediatorImpl;

@Configuration
public class MediatorConfig {
    @Bean
    public Mediator getMediator(ApplicationContext ctx) {
        return new MediatorImpl(ctx);
    }
}
