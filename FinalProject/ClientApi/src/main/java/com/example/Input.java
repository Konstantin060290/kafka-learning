package com.example;

import com.example.commands.GetRecommendationsQuery;
import com.example.commands.SearchCommand;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Input {

    @Autowired
    SearchCommand _searchCommand;

    @Autowired
    GetRecommendationsQuery _getRecommendationsQuery;

    @PostConstruct
    public void Input(){
        Scanner scanner = new Scanner(System.in);
        while (true)
        {
            System.out.println("\nAvailable commands:");
            System.out.println("1. search products");
            System.out.println("2. get recommendations");
            System.out.println("3. exit the application");
            System.out.print("Enter command number: ");

            String input = scanner.nextLine();
            if ("3".equalsIgnoreCase(input.trim())) {
                break;
            }

            try {
                switch (input) {
                    case "1":
                        _searchCommand.Search();
                        break;
                    case "2":
                        _getRecommendationsQuery.GetRecommendations();
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}
