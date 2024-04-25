package com.toleyko.spring.springboot.userservice.service;

import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handler.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.UserAlreadyExistException;
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

public class UserServiceTest {

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
    void testCreateUser_SuccessfulCreation() throws UserAlreadyExistException, BadUserDataException, URISyntaxException {

        Response successfulResponse = Response.status(Response.Status.CREATED).location(new URI("/api/users")).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(successfulResponse);
        UserService userService = new UserService();
        userService.setKeycloak(keycloak);
        UserDTO userDTO = new UserDTO("johnDoe", "john@example.com", "password", "Kir", "Tol");

        UserResource userResource = mock(UserResource.class);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(this.usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        UserRepresentation result = userService.createUser(userDTO);

        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertTrue(result.isEmailVerified());
    }

    @Test
    void UserService_CreateUser_UserAlreadyExistExceptionTest() {

        Response conflictResponse = Response.status(Response.Status.CONFLICT).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(conflictResponse);
        UserService userService = new UserService();
        userService.setKeycloak(keycloak);
        UserDTO userDTO = new UserDTO("johnDoe", "john@example.com", "password", "Kir", "Tol");

        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    @Test
    void UserService_CreateUser_BadUserDataExceptionTest() {

        Response badResponse = Response.status(Response.Status.BAD_REQUEST).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(badResponse);
        UserService userService = new UserService();
        userService.setKeycloak(keycloak);
        UserDTO userDTO = new UserDTO("johnDoe", "john@example.com", "password", "Kir", "Tol");

        Assertions.assertThrows(BadUserDataException.class, () -> {
            userService.createUser(userDTO);
        });
    }

    @Test
    public void UserService_getUserByUsername_successfulCreationTest() {
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

        UserService userService = new UserService();
        userService.setKeycloak(keycloak);
        UserRepresentation result = userService.getUserByUsername(username);

        Assertions.assertEquals(userRepresentation, result);
    }

    @Test
    public void UserService_getUserByUsername_NoSuchUserExceptionTest() {
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

        UserService userService = new UserService();
        userService.setKeycloak(keycloak);

        Assertions.assertThrows(NoSuchUserException.class, () -> {
            userService.getUserByUsername(username);
        });
    }

}
