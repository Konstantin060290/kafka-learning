package com.example;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.Scanner;

@Component
public class Input {

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
                        if (parts.length > 1) {
                            searchCommand.execute(parts[1]);
                        } else {
                            System.out.println("Please provide product name");
                        }
                        break;
                    case "2":
                        if (parts.length > 1) {
                            recommendCommand.execute(parts[1]);
                        } else {
                            System.out.println("Please provide user ID");
                        }
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
