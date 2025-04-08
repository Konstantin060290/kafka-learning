package com.example.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class Order {
    @JsonProperty("OrderId")
    public UUID OrderId;
    @JsonProperty("CustomerEmail")
    public String CustomerEmail;
    @JsonProperty("Goods")
    public ArrayList<Good> Goods;
}
