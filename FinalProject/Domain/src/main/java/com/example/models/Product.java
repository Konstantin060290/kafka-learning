package com.example.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private int id;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "rest_information", columnDefinition = "TEXT")
    private String restInformation;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int id) { this.productId = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRestInformation() { return restInformation; }
    public void setRestInformation(String restInformation) { this.restInformation = restInformation; }
}
