package com.example.Task1.Contracts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Good
{
    @JsonProperty("Name")
    public String Name;
    @JsonProperty("Qty")
    public int Qty;
}
