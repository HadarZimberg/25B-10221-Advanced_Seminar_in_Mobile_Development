package com.example.apiserver.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @PostMapping("/echo")
    public String echoMessage(@RequestBody String message) {
        return message;
    }
    
    @RestController
    public class RootController {
        @GetMapping("/")
        public String index() {
            return "🟢 API Server is running!";
        }
    }
}
