package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories("com.example.repositories")
@EntityScan("com.example.models")
@ComponentScan("com.examples.kafka")
//@EnableConfigurationProperties(ClientApiOptions.class)
public class ClientApi {
    public static void main( String[] args )
    {
        SpringApplication.run(ClientApi.class, args);
    }
}
