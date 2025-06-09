package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication()
@EnableJpaRepositories("com.example.repositories")
@EntityScan("com.example.models")
@ComponentScan("com.examples.kafka")
public class ClientApi {

    public static void main( String[] args )
    {
        SpringApplication.run(ClientApi.class, args);
    }
}
