package com.toleyko.spring.springboot.userservice.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String hiMsg() {
        return "hi";
    }

    @GetMapping("/test")
    public String testMsg() {
        return "test";
    }
}
