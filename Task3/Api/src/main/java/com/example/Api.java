package com.example;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Api {


    @Autowired
    private ProhibitedWordsStoreFiller prohibitedWordsFilter;
    @Autowired
    //private ProhibitedWordsStoreFiller prohibitedWordsFilter;

    public static void main(String[] args) {
        SpringApplication.run(Api.class, args);
    }

    @PostConstruct
    public void init() {
        prohibitedWordsFilter.FillStore();
    }
}
