package com.example.controllers;

import com.example.Message;
import com.example.commands.ProduceMessageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shortbus.Mediator;
import shortbus.Response;

@RestController
public class MessagesController {
    @Autowired
    Mediator mediator;
    @PostMapping("/api/produce-message")
    public ResponseEntity<?> produceMessage(
            @RequestBody Message message
    ) throws Exception {

        ProduceMessageCommand command = new ProduceMessageCommand(message);

        Response<Boolean> result = mediator.request(command);

        if(result.data.booleanValue() == false)
        {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
