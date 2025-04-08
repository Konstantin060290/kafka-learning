package com.example.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class Good
{
    @JsonProperty("Name")
    public String Name;
    @JsonProperty("Qty")
    public int Qty;
}
