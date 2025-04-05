package org.example.Contracts;

//import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

//@Data
public class OrderMessage {
    public int Version = 1;
    public ZonedDateTime DateTime;
    public Order Order;
    public UUID Id;
}
