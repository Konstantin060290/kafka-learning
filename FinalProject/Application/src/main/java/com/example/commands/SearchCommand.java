package com.example.commands;

import com.example.repositories.ProductsRepository;
import com.examples.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SearchCommand {

    @Autowired
    ProductsRepository _productsRepository;

    @Autowired
    Producer _clientApiProducer;

    public void Search()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");

        String input = scanner.nextLine();

        _clientApiProducer.Produce(input, "users-search-requests");

        var products = _productsRepository.findByName(input);

        if (products.isEmpty()) {
            System.out.println("No products found with name: " + input);
        } else {
            System.out.println("Found products:");
            products.forEach(p -> System.out.println(
                    "ID: " + p.getId() +
                            ", Name: " + p.getName() +
                            ", Rest Info: " + p.getRestInformation()
            ));
        }
    }
}
