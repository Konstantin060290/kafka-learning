package com.example.controllers;

import com.example.application.ProduceMessageCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shortbus.Mediator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * Controller for producing messages
 */
@RestController
public class MessagesController {

    //@AutoWired
    Mediator mediator;

    @RequestMapping(value = "/api/{name}/produce", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String produceMessage() {

        var command = new ProduceMessageCommand();

        //var result =
        return "";

    }
}
