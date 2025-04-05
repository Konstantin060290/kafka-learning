package com.example.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class OrderMessage {
    @JsonProperty("Version")
    public int Version = 1;
    @JsonProperty("DateTime")
    public ZonedDateTime DateTime;
    @JsonProperty("Order")
    public Order Order;
    @JsonProperty("Id")
    public UUID Id;
}
