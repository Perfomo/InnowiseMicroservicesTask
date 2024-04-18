package com.toleyko.spring.springboot.userservice.controllers;


import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @GetMapping("/user/{userName}")
    public List<UserRepresentation> getUser(@PathVariable String userName) {
        return userService.getUserByUsername(userName);
    }

    @GetMapping("/test")
    public String testMsg() {
        return "test";
    }

    @GetMapping("/users")
    public List<UserRepresentation> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return userService.createUser(userDTO);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
