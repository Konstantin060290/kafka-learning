package com.example.commands;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class SearchCommand {

    public void Search()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter product name: ");

        String input = scanner.nextLine();

        //отправим в kafka

        {

        }
    }
}
