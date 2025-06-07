package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scala.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {
    List<Product> findByName(String name);
}
