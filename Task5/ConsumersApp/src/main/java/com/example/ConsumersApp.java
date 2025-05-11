package com.example;

import com.example.consumers.OrdersConsumer;
import com.example.consumers.UsersConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ConsumersApp
{
    @Autowired
    private OrdersConsumer ordersConsumer;
    @Autowired
    private UsersConsumer usersConsumer;

    public static void main( String[] args )
    {
        SpringApplication.run(ConsumersApp.class, args);
    }
}
