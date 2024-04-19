package com.toleyko.spring.springboot.userservice.controllers;

import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handler.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public List<UserRepresentation> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistException, BadUserDataException {
        return userService.createUser(userDTO);
    }

    @GetMapping("/users/{userName}")
    public UserRepresentation getUser(@PathVariable String userName, Principal principal) throws AccessDeniedException {
        if (userName.equals(principal.getName())) {
            return userService.getUserByUsername(userName);
        }
        throw new AccessDeniedException("Access denied.");
    }

    @PutMapping("/users/{userName}")
    public UserRepresentation updateUser(@PathVariable String userName, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userName, userDTO);
    }

    @DeleteMapping("/users/{userName}")
    public void deleteUserByUserName(@PathVariable String userName) {
        userService.deleteUser(userName);
    }

    @GetMapping("/test")
    public String testMsg() {
        return "test";
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
