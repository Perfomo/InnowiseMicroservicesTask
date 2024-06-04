package com.toleyko.springboot.userservice.controller;

import com.toleyko.springboot.userservice.dto.User;
import com.toleyko.springboot.userservice.handler.UserPermissionHandler;
import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.springboot.userservice.service.UserKeycloakService;
import com.toleyko.springboot.userservice.service.kafka.KafkaToOrderMessagePublisher;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserKeycloakService userKeycloakService;
    private UserPermissionHandler userPermissionHandler;
    private KafkaToOrderMessagePublisher publisher;

    @Value("${GATEWAY_SERVICE_PORT}")
    private String gatewayPort;

    @GetMapping("/users")
    public List<UserRepresentation> getAllUsers() throws ForbiddenException {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"))) {
            return userKeycloakService.getUsers();
        }
        throw new ForbiddenException("Access denied");
    }

    @PostMapping("/users")
    public UserRepresentation createUser(@Valid @RequestBody User user) throws UserAlreadyExistException, BadUserDataException {
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
    public UserRepresentation updateUser(@PathVariable String userName, @Valid @RequestBody User user) throws ForbiddenException, BadUserDataException {
        if (userPermissionHandler.isPermitted(userName)) {
            if (!userName.equals(user.getUsername())) {
                return userKeycloakService.updateUser(userName, user);
            }
            throw new BadUserDataException("Username changing is not permitted");
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
    public RedirectView logout() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://172.17.0.1:" + gatewayPort + "/logout");
        return redirectView;
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
