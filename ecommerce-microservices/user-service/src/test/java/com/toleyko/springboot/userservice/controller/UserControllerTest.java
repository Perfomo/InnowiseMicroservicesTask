package com.toleyko.springboot.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.userservice.dto.User;
import com.toleyko.springboot.userservice.handler.GlobalUserHandler;
import com.toleyko.springboot.userservice.handler.UserPermissionHandler;
import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.springboot.userservice.service.UserKeycloakService;
import com.toleyko.springboot.userservice.service.kafka.KafkaToOrderMessagePublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebSecurity
public class UserControllerTest {

    @Mock
    private UserKeycloakService userKeycloakService;
    @Mock
    private UserPermissionHandler userPermissionHandler;
    @Mock
    private KafkaToOrderMessagePublisher publisher;
    private MockMvc mockMvc;

    private UserRepresentation createTestUser(String username) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        return user;
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController userController = new UserController();
        userController.setUserService(userKeycloakService);
        userController.setPermissionHandler(userPermissionHandler);
        userController.setPublisher(publisher);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalUserHandler())
                .build();
    }

    @Test
    public void getAllUsers_SuccessfulTest() throws Exception {
        List<UserRepresentation> expectedUsers = new ArrayList<>();
        expectedUsers.add(this.createTestUser("user1"));
        expectedUsers.add(this.createTestUser("user2"));

        Authentication authentication = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(userKeycloakService.getUsers()).thenReturn(expectedUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userKeycloakService, times(1)).getUsers();
    }

    @Test
    public void getAllUsers_ForbiddenExceptionTest() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("User")));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .with(authentication(authentication)))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()));

        verify(userKeycloakService, times(0)).getUsers();
    }

    @Test
    public void createUser_SuccessfulCreationTest() throws Exception {
        User user = new User("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userKeycloakService.createUser(user)).thenReturn(userRepresentation);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andReturn();

        verify(userKeycloakService, times(1)).createUser(user);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseBody);
    }

    @Test
    public void createUser_UserAlreadyExistExceptionTest() throws Exception {
        User user = new User("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userKeycloakService.createUser(user)).thenThrow(new UserAlreadyExistException("msg"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isConflict())
                .andExpect(result -> Assertions.assertInstanceOf(UserAlreadyExistException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("msg", result.getResolvedException().getMessage()));

        verify(userKeycloakService, times(1)).createUser(user);
    }

    @Test
    public void createUser_BadUserDataExceptionTest() throws Exception {
        User user = new User("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userKeycloakService.createUser(user)).thenThrow(new BadUserDataException("msg"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(BadUserDataException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("msg", result.getResolvedException().getMessage()));

        verify(userKeycloakService, times(1)).createUser(user);
    }

    @Test
    public void getUser_SuccessfulTest() throws Exception {
        String username = "per";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        when(userKeycloakService.getUserByUsername(username)).thenReturn(userRepresentation);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(userRepresentation), responseBody);
        verify(userKeycloakService, times(1)).getUserByUsername(username);
    }

    @Test
    public void getUser_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        doReturn(false).when(userPermissionHandler).isPermitted(username);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", username))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()));

        verify(userKeycloakService, times(0)).getUserByUsername(username);
    }

    @Test
    public void updateUser_SuccessfulTest() throws Exception {
        String username = "per";
        UserRepresentation userRepresentation = new UserRepresentation();
        User user = new User("per", "2@d", "pass", "name", "las");
        userRepresentation.setUsername(username);
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        when(userKeycloakService.updateUser(username, user)).thenReturn(userRepresentation);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(userRepresentation), responseBody);
        verify(userKeycloakService, times(1)).updateUser(username, user);
    }

    @Test
    public void updateUser_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        User user = new User("per", "2@d", "pass", "name", "las");
        doReturn(false).when(userPermissionHandler).isPermitted(username);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()));

        verify(userKeycloakService, times(0)).updateUser(username, user);
    }

    @Test
    public void deleteUserByUserName_SuccessfulTest() throws Exception {
        String username = "per";
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        doNothing().when(userKeycloakService).deleteUser(username);
        doNothing().when(publisher).sendMessageToTopic(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", username))
                .andExpect(status().isOk());
        verify(userKeycloakService, times(1)).deleteUser(username);
    }

    @Test
    public void deleteUserByUserName_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        doReturn(false).when(userPermissionHandler).isPermitted(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", username))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()));

        verify(userKeycloakService, times(0)).deleteUser(username);
    }

}
