package com.example.commands;

import shortbus.Request;

public class AddProhibitedWordCommand implements Request<Boolean> {
    public String word;
    public AddProhibitedWordCommand(String word)
    {
        this.word = word;
    }
}
