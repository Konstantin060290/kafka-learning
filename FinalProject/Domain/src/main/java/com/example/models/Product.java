package com.example.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(name = "name", nullable = false, length = 100)
    public  String name;

    @Column(name = "rest_information", columnDefinition = "TEXT")
    private  String restInformation;
}
