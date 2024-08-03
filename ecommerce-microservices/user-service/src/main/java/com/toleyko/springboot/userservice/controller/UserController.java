package com.toleyko.springboot.userservice.controller;

import com.toleyko.springboot.userservice.dto.User;
import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.springboot.userservice.service.UserKeycloakService;
import jakarta.validation.Valid;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserKeycloakService userKeycloakService;

    @Value("${GATEWAY_SERVICE_PORT}")
    private String gatewayPort;
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<UserRepresentation>> getAllUsers() {
        return ResponseEntity.ok(userKeycloakService.getUsers());
    }
    @PostMapping("/users")
    public ResponseEntity<UserRepresentation> createUser(@Valid @RequestBody User user) throws UserAlreadyExistException, BadUserDataException {
        return ResponseEntity.ok(userKeycloakService.createUser(user));
    }
    @GetMapping("/users/{userName}")
    @PreAuthorize("authentication.name == #userName || hasRole('ROLE_MANAGER')")
    public ResponseEntity<UserRepresentation> getUser(@PathVariable String userName) {
        return ResponseEntity.ok(userKeycloakService.getUserByUsername(userName));
    }

    @PutMapping("/users/{userName}")
    @PreAuthorize("authentication.name == #userName || hasRole('ROLE_MANAGER')")
    public ResponseEntity<UserRepresentation> updateUser(@PathVariable String userName, @Valid @RequestBody User user) throws BadUserDataException {
        return ResponseEntity.ok(userKeycloakService.updateUser(userName, user));
    }

    @DeleteMapping("/users/{userName}")
    @PreAuthorize("authentication.name == #userName || hasRole('ROLE_MANAGER')")
    public void deleteUserByUserName(@PathVariable String userName) {
        userKeycloakService.deleteUser(userName);
    }

    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout(Principal principal) {
        userKeycloakService.logoutKeycloakUser(principal.getName());
    }

    @Autowired
    public UserController(UserKeycloakService userKeycloakService) {
        this.userKeycloakService = userKeycloakService;
    }
}
