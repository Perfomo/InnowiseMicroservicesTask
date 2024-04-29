package com.toleyko.spring.springboot.userservice.controllers;

import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handlers.UserPermissionHandler;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ForbiddenException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    private UserPermissionHandler userPermissionHandler;
    @GetMapping("/users")
    public List<UserRepresentation> getUsers() throws ForbiddenException {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return userService.getUsers();
        }
        throw new ForbiddenException("Access denied");
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@RequestBody UserDTO userDTO) throws UserAlreadyExistException, BadUserDataException {
        return userService.createUser(userDTO);
    }

    @GetMapping("/users/{userName}")
    public UserRepresentation getUser(@PathVariable String userName) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            return userService.getUserByUsername(userName);
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @PutMapping("/users/{userName}")
    public UserRepresentation updateUser(@PathVariable String userName, @RequestBody UserDTO userDTO) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            return userService.updateUser(userName, userDTO);
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @DeleteMapping("/users/{userName}")
    public void deleteUserByUserName(@PathVariable String userName) throws ForbiddenException {
        if (userPermissionHandler.isPermitted(userName)) {
            userService.deleteUser(userName);
            return;
        }
        throw new ForbiddenException("You are not a " + userName);
    }

    @GetMapping("/logout")
    public void logoutUser() {
        userService.logoutUser();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPermissionHandler(UserPermissionHandler userPermissionHandler) {
        this.userPermissionHandler = userPermissionHandler;
    }
}
