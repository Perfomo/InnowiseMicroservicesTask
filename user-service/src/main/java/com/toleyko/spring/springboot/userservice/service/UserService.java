package com.toleyko.spring.springboot.userservice.service;

import com.toleyko.spring.springboot.userservice.Credentials;
import com.toleyko.spring.springboot.userservice.config.KeycloakConfig;
import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private Keycloak keycloak;

    @Autowired
    public void setKeycloak(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public UserRepresentation createUser(UserDTO userDTO) {
        UserRepresentation user = this.createUserRepresentation(userDTO);
        user.setRealmRoles(Collections.singletonList("ROLE_USER"));
        user.setEnabled(true);
        user.setEmailVerified(true);
//        System.out.println(keycloak.realm(KeycloakConfig.realm).users().create(user).getStatusInfo());
//        return null;
//                .getStatusInfo()
        return keycloak.realm(KeycloakConfig.realm).users().create(user)
                .getStatusInfo().equals(Response.Status.CREATED) ? user : null;
    }

    public List<UserRepresentation> getUsers() {
        return keycloak.realm(KeycloakConfig.realm).users().list();
    }

    public List<UserRepresentation> getUserByUsername(String userName) {
        return keycloak.realm(KeycloakConfig.realm).users().searchByUsername(userName, true);
    }

    public void updateUser(String userId, UserDTO userDTO) {
        UserRepresentation user = this.createUserRepresentation(userDTO);
        keycloak.realm(KeycloakConfig.realm).users().get(userId).update(user);
    }

    public void deleteUser(String userId) {
        keycloak.realm(KeycloakConfig.realm).users().delete(userId);
    }

    public void sendResetPassword(String userId){

        keycloak.realm(KeycloakConfig.realm).users().get(userId)
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    private UserRepresentation createUserRepresentation(UserDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(Credentials.createPasswordCredentials(userDTO.getPassword())));
        return user;
    }

}
