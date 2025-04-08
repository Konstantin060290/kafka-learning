package com.example.controllers;

import com.example.application.ProduceMessageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import shortbus.Mediator;
import shortbus.Response;

/**
 *
 * Controller for producing messages
 */
@RestController
public class MessagesController {

    @Autowired
    Mediator mediator;
    @RequestMapping(value = "/api/produce", method = RequestMethod.POST)
    public HttpStatus produceMessage() throws Exception {

        ProduceMessageCommand command = new ProduceMessageCommand();

        Response <Boolean> result = mediator.request(command);

        if(result.data.booleanValue() == false)
        {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }
}
