package com.toleyko.springboot.userservice.service;

import com.toleyko.springboot.userservice.dto.User;
import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.NoSuchUserException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.springboot.userservice.service.kafka.KafkaToOrderMessagePublisher;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserKeycloakServiceTest {
    @Mock
    private Keycloak keycloak;
    @Mock
    private UsersResource usersResource;
    @Mock
    private KafkaToOrderMessagePublisher publisher;
    private UserKeycloakService userKeycloakService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userKeycloakService = new UserKeycloakService(keycloak, publisher);
        RealmResource realmResource = mock(RealmResource.class);
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
        User user = new User("johnDoe", "john@example.com", "password", "Kir", "Tol");

        UserResource userResource = mock(UserResource.class);
        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(this.usersResource.get(anyString())).thenReturn(userResource);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        UserRepresentation result = userKeycloakService.createUser(user);

        assertNotNull(result);
        Assertions.assertTrue(result.isEnabled());
        Assertions.assertTrue(result.isEmailVerified());
    }

    @Test
    void createUser_UserAlreadyExistExceptionTest() {

        Response conflictResponse = Response.status(Response.Status.CONFLICT).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(conflictResponse);
        User user = new User("johnDoe", "john@example.com", "password", "Kir", "Tol");

        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            userKeycloakService.createUser(user);
        });
    }

    @Test
    void createUser_BadUserDataExceptionTest() {

        Response badResponse = Response.status(Response.Status.BAD_REQUEST).build();
        when(this.usersResource.create(any(UserRepresentation.class))).thenReturn(badResponse);

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

        Assertions.assertThrows(NoSuchUserException.class, () -> {
            userKeycloakService.getUserByUsername(username);
        });
    }
}
