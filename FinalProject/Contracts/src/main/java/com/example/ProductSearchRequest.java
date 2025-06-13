package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductSearchRequest {

    @JsonProperty("productName")
    public String productName;
}
