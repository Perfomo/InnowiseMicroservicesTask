package com.toleyko.spring.springboot.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handler.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
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
import java.util.Objects;

@AutoConfigureMockMvc
@EnableWebSecurity
public class UserControllerTest {

    @Mock
    private UserService userService;
    private UserController userController;
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
        userController = new UserController();
        userController.setUserService(userService);
        when(userController.isPermitted(anyString())).thenReturn(true);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void getUsers_AuthorisedTest() throws Exception {
        List<UserRepresentation> expectedUsers = new ArrayList<>();
        expectedUsers.add(this.createTestUser("user1"));
        expectedUsers.add(this.createTestUser("user2"));

        Authentication authentication = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        when(userService.getUsers()).thenReturn(expectedUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
        verify(userService, times(1)).getUsers();
    }

//    @Test
//    public void getUsers_UnAuthorisedTest() throws Exception {
//        Authentication authentication = new UsernamePasswordAuthenticationToken("manager", "manager",
//                Collections.singletonList(new SimpleGrantedAuthority("User")));
//
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authentication);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
//                        .with(authentication(authentication)))
//                .andExpect(status().isForbidden());
//                .andReturn();

//        Assertions.assertThrows(AccessDeniedException.class, () -> {
//            throw Objects.requireNonNull(result.getResolvedException());
//        });
//    }

    @Test
    public void createUser_SuccessfulCreationTest() throws Exception {
        UserDTO userDTO = new UserDTO("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userService.createUser(userDTO)).thenReturn(userRepresentation);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).createUser(userDTO);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseBody);
    }

//    @Test
//    public void createUser_UserAlreadyExistExceptionTest() throws Exception {
//        UserDTO userDTO = new UserDTO("Per","3@d","root", "Kir", "Tol");
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername("Per");
//        when(userService.createUser(userDTO)).thenThrow(new UserAlreadyExistException("msg"));
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(userDTO)))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        UserAlreadyExistException exception = Assertions.assertThrows(UserAlreadyExistException.class, () -> {
//            throw Objects.requireNonNull(result.getResolvedException());
//        });
//        Assertions.assertEquals("msg", exception.getMessage());
//    }

//    @Test
//    public void createUser_BadUserDataExceptionTest() throws Exception {
//        UserDTO userDTO = new UserDTO("Per","3@d","root", "Kir", "Tol");
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername("Per");
//        when(userService.createUser(userDTO)).thenThrow(new BadUserDataException("msg"));
//
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(userDTO)))
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//        BadUserDataException exception = Assertions.assertThrows(BadUserDataException.class, () -> {
//            throw Objects.requireNonNull(result.getResolvedException());
//        });
//        Assertions.assertEquals("msg", exception.getMessage());
//    }

//    @Test
//    public void getUser_SuccessfulTest() throws Exception {
//        String username = "per";
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername(username);
//        when(userController.isPermitted(username)).thenReturn(true);
//        when(userService.getUserByUsername(username)).thenReturn(userRepresentation);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", username))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").exists())
//                .andExpect(jsonPath("$.username").value(username));
//        verify(userService, times(1)).getUserByUsername(username);
//    }









}
