package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductsStreamOptions {
    @Value("${products-stream.application-id}")
    public String applicationId;

    @Value("${products-stream.prohibited-products-store-name}")
    public String prohibitedProductsStoreName;
}
