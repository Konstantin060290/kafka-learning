package org.example.Contracts;
//import lombok.*;

import java.util.List;
import java.util.UUID;


//@Data
public class Order {
    public UUID OrderId;
    public String CustomerEmail;
    public List<Good> Goods;
}

