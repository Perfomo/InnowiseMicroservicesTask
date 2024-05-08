package com.toleyko.spring.springboot.userservice.controller;

import com.toleyko.spring.springboot.userservice.dto.User;
import com.toleyko.spring.springboot.userservice.handler.UserPermissionHandler;
import com.toleyko.spring.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.service.KafkaToOrderMessagePublisher;
import com.toleyko.spring.springboot.userservice.service.UserKeycloakService;
import jakarta.ws.rs.ForbiddenException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserKeycloakService userKeycloakService;
    private UserPermissionHandler userPermissionHandler;
    private KafkaToOrderMessagePublisher publisher;
    @GetMapping("/users")
    public List<UserRepresentation> getAllUsers() throws ForbiddenException {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return userKeycloakService.getUsers();
        }
        throw new ForbiddenException("Access denied");
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@RequestBody User user) throws UserAlreadyExistException, BadUserDataException {
        return userKeycloakService.createUser(user);
    }

    @GetMapping("/users/{userName}")
    public UserRepresentation getUser(@PathVariable String userName) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            return userKeycloakService.getUserByUsername(userName);
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @PutMapping("/users/{userName}")
    public UserRepresentation updateUser(@PathVariable String userName, @RequestBody User user) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            return userKeycloakService.updateUser(userName, user);
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @DeleteMapping("/users/{userName}")
    public void deleteUserByUserName(@PathVariable String userName) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            userKeycloakService.deleteUser(userName);
            publisher.sendMessageToTopic(userName);
            return;
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @GetMapping("/logout")
    public void logoutUser() {
        userKeycloakService.logoutUser();
    }

    @Autowired
    public void setPublisher(KafkaToOrderMessagePublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired
    public void setUserService(UserKeycloakService userKeycloakService) {
        this.userKeycloakService = userKeycloakService;
    }

    @Autowired
    public void setPermissionHandler(UserPermissionHandler userPermissionHandler) {
        this.userPermissionHandler = userPermissionHandler;
    }
}
