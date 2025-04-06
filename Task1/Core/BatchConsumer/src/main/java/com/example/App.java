package com.example;

import com.example.application.ConsumeMessageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shortbus.Mediator;

@SpringBootApplication
public class App implements CommandLineRunner
{
    @Autowired
    Mediator mediator;

    public App(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void run(String... args) {

        ConsumeMessageCommand command = new ConsumeMessageCommand();

        mediator.request(command);
    }

    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
