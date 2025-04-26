package com.example.controllers;

import com.example.commands.AddProhibitedWordCommand;
import com.example.commands.BlockUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shortbus.Mediator;
import shortbus.Response;

@RestController
public class WordsController {
    @Autowired
    Mediator mediator;
    @PostMapping("/api/add-prohibited-word")
    public ResponseEntity<?> addProhibitedWord(
            @RequestParam("prohibitedWord") String prohibitedWord
    ) throws Exception {

        AddProhibitedWordCommand command = new AddProhibitedWordCommand(prohibitedWord);

        Response<Boolean> result = mediator.request(command);

        if(result.data.booleanValue() == false)
        {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
