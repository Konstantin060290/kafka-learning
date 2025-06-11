package com.example.commands;

import com.example.ProductSearchRequest;
import com.example.TopicsOptions;
import com.example.repositories.ProductsRepository;
import com.examples.kafka.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SearchCommand {

    @Autowired
    ProductsRepository _productsRepository;

    @Autowired
    Producer _clientApiProducer;

    @Autowired
    TopicsOptions topicsOptions;

    public void Search() throws JsonProcessingException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");

        String input = scanner.nextLine();

        var searchRequest = new ProductSearchRequest();

        searchRequest.productName = input;

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(searchRequest);

        _clientApiProducer.Produce(jsonString, topicsOptions.usersSearchRequestsTopicName);

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
