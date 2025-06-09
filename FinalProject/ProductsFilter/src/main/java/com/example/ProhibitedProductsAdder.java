package com.example;

import com.examples.kafka.Producer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ProhibitedProductsAdder {

    @Autowired
    Producer producer;

    @PostConstruct
    public void Input(){
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println("Enter prohibited product name:");

            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }

            producer.Produce(input, "blocked-products");
        }
    }
}
