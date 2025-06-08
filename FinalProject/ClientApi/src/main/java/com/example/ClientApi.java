package com.example;

import com.examples.kafka.ProductsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@EnableJpaRepositories("com.example.repositories")
@EntityScan("com.example.models")
@ComponentScan("com.examples.kafka")
public class ClientApi {

    @Autowired
    ProductsConsumer productsConsumer;

    public static void main( String[] args )
    {
        SpringApplication.run(ClientApi.class, args);
    }
}
