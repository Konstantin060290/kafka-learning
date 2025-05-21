package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ConsumersApp
{
    @Autowired
    public ConsumerTopic1 consumer;

    public static void main( String[] args )
    {
        SpringApplication.run(ConsumersApp.class, args);
    }
}
