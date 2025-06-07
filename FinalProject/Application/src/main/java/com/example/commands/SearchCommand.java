package com.example.commands;

import com.example.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Scanner;

@Component
public class SearchCommand {

    @Autowired
    ProductsRepository _productsRepository;

    public void Search()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");

        String input = scanner.nextLine();

        //отправим в kafka поисковый запрос

        var products = _productsRepository.findByName(input).toArray();

        if (products.length == 0) {
            System.out.println("No products found with name: " + input);
        } else {
            System.out.println("Found products:");
            Arrays.stream(products).forEach(System.out::println);
        }
    }
}
