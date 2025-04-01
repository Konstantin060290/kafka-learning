package com.example.Task1.Contracts;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Data
public class Order {
    @JsonProperty("OrderId")
    public UUID OrderId;
    @JsonProperty("CustomerEmail")
    public String CustomerEmail;
    @JsonProperty("Goods")
    public List<Good> Goods;
}

