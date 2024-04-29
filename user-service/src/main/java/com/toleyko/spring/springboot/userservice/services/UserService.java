package com.toleyko.spring.springboot.userservice.services;

import com.toleyko.spring.springboot.userservice.Credentials;
import com.toleyko.spring.springboot.userservice.configs.KeycloakConfig;
import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.UserAlreadyExistException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.common.util.Time;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private Keycloak keycloak;
    @Autowired
    public void setKeycloak(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UserRepresentation createUser(UserDTO userDTO) throws UserAlreadyExistException, BadUserDataException {
        UserRepresentation user = this.createUserRepresentation(userDTO);
        user.setEnabled(true);
        user.setEmailVerified(true);

        Response response = keycloak.realm(KeycloakConfig.realm)
                .users()
                .create(user);

        if (response.getStatus() == 409) {
            throw new UserAlreadyExistException("User with this username or email already exists.");
        }
        if (response.getStatus() == 400) {
            throw new BadUserDataException("Bad user data.");
        }

        if (response.getStatusInfo().equals(Response.Status.CREATED)) {
            RoleRepresentation role = keycloak.realm(KeycloakConfig.realm)
                    .roles()
                    .get("ROLE_USER")
                    .toRepresentation();
            List<RoleRepresentation> roles = Collections.singletonList(role);
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            keycloak.realm(KeycloakConfig.realm).users().get(userId).roles().realmLevel().add(roles);
            return user;
        }
        return null;
    }

    public List<UserRepresentation> getUsers() {
        return keycloak.realm(KeycloakConfig.realm).users().list();
    }

    public UserRepresentation getUserByUsername(String userName) {
        List<UserRepresentation> userRepresentation = keycloak.realm(KeycloakConfig.realm).users().searchByUsername(userName, true);
        if (userRepresentation.isEmpty()) {
            throw new NoSuchUserException("Can't find user with " + userName + " username.");
        }
        return userRepresentation.getFirst();
    }

    public UserRepresentation updateUser(String userName, UserDTO userDTO) {
        UserRepresentation user = this.getUserByUsername(userName);
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(Credentials.createPasswordCredentials(userDTO.getPassword())));
        keycloak.realm(KeycloakConfig.realm).users().get(user.getId()).update(user);
        return this.getUserByUsername(userName);
    }

    public void deleteUser(String userName) {
        UserRepresentation user = this.getUserByUsername(userName);
        keycloak.realm(KeycloakConfig.realm).users().get(user.getId()).logout();
        keycloak.realm(KeycloakConfig.realm).users().delete(user.getId());
    }

    public void logoutUser() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        keycloak.realm(KeycloakConfig.realm).users().get(id).logout();
    }

    private UserRepresentation createUserRepresentation(UserDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(Credentials.createPasswordCredentials(userDTO.getPassword())));
        user.setCreatedTimestamp(Time.currentTimeMillis());
        return user;
    }
}
