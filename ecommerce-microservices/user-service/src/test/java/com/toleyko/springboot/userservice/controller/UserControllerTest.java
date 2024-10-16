package com.toleyko.springboot.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.userservice.dto.User;
import com.toleyko.springboot.userservice.handler.GlobalUserHandler;
import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import com.toleyko.springboot.userservice.service.UserKeycloakService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    @Mock
    private UserKeycloakService userKeycloakService;
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
        UserController userController = new UserController(userKeycloakService);
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
    public void createUser_SuccessfulCreationTest() throws Exception {
        User user = new User("Perfomo","3sd@gmail.com","root", "Kir", "Tol");
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
        User user = new User("Perfomo","3sd@gmail.com","root", "Kir", "Tol");
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
        User user = new User("Perfomo","3sd@gmail.com","root", "Kir", "Tol");
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
    public void createUser_BeanValidationExceptionTest() throws Exception {
        User user = new User("","@gmail.com","ro", "", "");
        Map<String, String> expected = new HashMap<>();
        expected.put("lastName", "Invalid lastname");
        expected.put("firstName", "Invalid firstname");
        expected.put("password", "Invalid password, length must be more that 3 and less than 16");
        expected.put("email", "Invalid email");
        expected.put("username", "Invalid length of username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        HashMap resultMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        Assertions.assertEquals(expected, resultMap);
    }

    @Test
    public void getUser_SuccessfulTest() throws Exception {
        String username = "per";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
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
    public void updateUser_SuccessfulTest() throws Exception {
        String username = "Perfomo";
        UserRepresentation userRepresentation = new UserRepresentation();
        User user = new User("Perfomo","3sd@gmail.com","root", "Kir", "Tol");
        userRepresentation.setUsername(username);
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
    public void updateUser_BeanValidationExceptionTest() throws Exception {
        User user = new User("","@gmail.com","ro", "", "");
        HashMap<String, String> expected = new HashMap<>();
        expected.put("firstName", "Invalid firstname");
        expected.put("lastName", "Invalid lastname");
        expected.put("password", "Invalid password, length must be more that 3 and less than 16");
        expected.put("email", "Invalid email");
        expected.put("username", "Invalid length of username");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{username}", "Perfomo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        HashMap resultMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        Assertions.assertEquals(expected, resultMap);
    }

    @Test
    public void deleteUserByUserName_SuccessfulTest() throws Exception {
        String username = "per";
        doNothing().when(userKeycloakService).deleteUser(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", username))
                .andExpect(status().isOk());
        verify(userKeycloakService, times(1)).deleteUser(username);
    }

    @Test
    public void logoutTest() throws Exception {
        Authentication authentication2 = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));
        doNothing().when(userKeycloakService).logoutKeycloakUser(anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/logout")
                        .principal(authentication2))
                .andExpect(status().isOk());
    }
}
