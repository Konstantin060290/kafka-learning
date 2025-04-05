package com.example.Task1.Controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello")
class HelloController {
    @GetMapping
    public String sayHello() {
        return "Hello from Spring Boot!";
    }
}
