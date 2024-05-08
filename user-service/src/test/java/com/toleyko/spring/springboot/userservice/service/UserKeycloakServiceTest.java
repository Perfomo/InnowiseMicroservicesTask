package com.toleyko.spring.springboot.userservice.service;

import com.toleyko.spring.springboot.userservice.dto.User;
import com.toleyko.spring.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exception.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handler.exception.UserAlreadyExistException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserKeycloakServiceTest {
    private Keycloak keycloak;
    private UsersResource usersResource;
    @BeforeEach
    public void setUp() {
        this.keycloak = mock(Keycloak.class);
        RealmResource realmResource = mock(RealmResource.class);
        this.usersResource = mock(UsersResource.class);
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(this.usersResource);

        RolesResource rolesResource = mock(RolesResource.class);
        RoleResource roleResource = mock(RoleResource.class);
        RoleRepresentation roleRepresentation = mock(RoleRepresentation.class);
        when(realmResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get(anyString())).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(roleRepresentation);
    }

    @Test
    void createUser_SuccessfulTest() throws UserAlreadyExistException, BadUserDataException, URISyntaxException {

        Response successfulResponse = Response.status(Response.Status.CREATED).location(new URI("/api/users")).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(successfulResponse);
        UserKeycloakService userKeycloakService = new UserKeycloakService();
        userKeycloakService.setKeycloak(keycloak);
        User user = new User("johnDoe", "john@example.com", "password", "Kir", "Tol");

        UserResource userResource = mock(UserResource.class);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(this.usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        UserRepresentation result = userKeycloakService.createUser(user);

        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertTrue(result.isEmailVerified());
    }

    @Test
    void createUser_UserAlreadyExistExceptionTest() {

        Response conflictResponse = Response.status(Response.Status.CONFLICT).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(conflictResponse);
        UserKeycloakService userKeycloakService = new UserKeycloakService();
        userKeycloakService.setKeycloak(keycloak);
        User user = new User("johnDoe", "john@example.com", "password", "Kir", "Tol");

        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            userKeycloakService.createUser(user);
        });
    }

    @Test
    void createUser_BadUserDataExceptionTest() {

        Response badResponse = Response.status(Response.Status.BAD_REQUEST).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(badResponse);
        UserKeycloakService userKeycloakService = new UserKeycloakService();
        userKeycloakService.setKeycloak(keycloak);
        User user = new User("johnDoe", "john@example.com", "password", "Kir", "Tol");

        Assertions.assertThrows(BadUserDataException.class, () -> {
            userKeycloakService.createUser(user);
        });
    }

    @Test
    public void getUserByUsername_successfulTest() {
        String username = "testUser";
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentations.add(userRepresentation);
        when(this.usersResource.searchByUsername(eq(username), eq(true))).thenAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(0).equals(username) && invocationOnMock.getArgument(1).equals(true)) {
                return userRepresentations;
            }
            return null;
        });

        UserKeycloakService userKeycloakService = new UserKeycloakService();
        userKeycloakService.setKeycloak(keycloak);
        UserRepresentation result = userKeycloakService.getUserByUsername(username);

        Assertions.assertEquals(userRepresentation, result);
    }

    @Test
    public void getUserByUsername_NoSuchUserExceptionTest() {
        String username = "testUser";
        List<UserRepresentation> userRepresentations = new ArrayList<>();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentations.add(userRepresentation);
        when(this.usersResource.searchByUsername(not(eq(username)), eq(true))).thenAnswer(invocationOnMock -> {
            if (invocationOnMock.getArgument(0).equals(username) && invocationOnMock.getArgument(1).equals(true)) {
                return userRepresentations;
            }
            return null;
        });

        UserKeycloakService userKeycloakService = new UserKeycloakService();
        userKeycloakService.setKeycloak(keycloak);

        Assertions.assertThrows(NoSuchUserException.class, () -> {
            userKeycloakService.getUserByUsername(username);
        });
    }
}
