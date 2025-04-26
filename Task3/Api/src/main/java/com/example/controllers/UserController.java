package com.example.controllers;

import com.example.commands.BlockUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortbus.Mediator;
import shortbus.Response;

@RestController
public class UserController {
    @Autowired
    Mediator mediator;
    @PostMapping("/api/block-user")
    public ResponseEntity<?> blockUser(
            @RequestParam("userName") String userName,
            @RequestParam("blockedUser") String blockedUser
    ) throws Exception {

        BlockUserCommand command = new BlockUserCommand(userName, blockedUser);

        Response<Boolean> result = mediator.request(command);

        if(result.data.booleanValue() == false)
        {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
