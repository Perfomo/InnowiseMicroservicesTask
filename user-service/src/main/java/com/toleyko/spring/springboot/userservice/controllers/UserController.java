package com.toleyko.spring.springboot.userservice.controllers;

import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handler.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public List<UserRepresentation> getUsers() throws AccessDeniedException {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return userService.getUsers();
        }
        throw new AccessDeniedException("Access denied");
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistException, BadUserDataException {
        return userService.createUser(userDTO);
    }

    @GetMapping("/users/{userName}")
    public UserRepresentation getUser(@PathVariable String userName) throws AccessDeniedException {
        if (this.isPermitted(userName)) {
            return userService.getUserByUsername(userName);
        }
        throw new AccessDeniedException("You are not a " + userName);
    }

    @PutMapping("/users/{userName}")
    public UserRepresentation updateUser(@PathVariable String userName, @RequestBody UserDTO userDTO) throws AccessDeniedException, UserAlreadyExistException, BadUserDataException {
        if (this.isPermitted(userName)) {
            return userService.updateUser(userName, userDTO);
        }
        throw new AccessDeniedException("You are not a " + userName);
    }

    @DeleteMapping("/users/{userName}")
    public void deleteUserByUserName(@PathVariable String userName) throws AccessDeniedException {
        if (this.isPermitted(userName)) {
            userService.deleteUser(userName);
        }
        throw new AccessDeniedException("You are not a " + userName);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private boolean isPermitted(String userName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRepresentation user = userService.getUserByUsername(userName);
        return user.getUsername().equals(authentication.getName()) || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }
}
