package com.example.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
