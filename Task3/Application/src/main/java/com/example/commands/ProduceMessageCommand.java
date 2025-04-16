package com.example.commands;

import com.example.Message;
import shortbus.Request;

public class ProduceMessageCommand  implements Request<Boolean> {
    public Message message;

    public ProduceMessageCommand(Message message)
    {
        this.message = message;
    }
}