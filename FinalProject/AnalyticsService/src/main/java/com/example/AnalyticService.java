package com.example;

import com.examples.kafka.RequestsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableAsync
@SpringBootApplication()
@ComponentScan("com.examples.kafka")
public class AnalyticService {

    @Autowired
    RequestsConsumer requestsConsumer;

    @Autowired
    HadoopDataAnalyser hadoopDataAnalyser;

    public static void main( String[] args )
    {
        SpringApplication.run(AnalyticService.class, args);
    }
}
