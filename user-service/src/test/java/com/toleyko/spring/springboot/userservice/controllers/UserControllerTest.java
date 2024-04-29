package com.toleyko.spring.springboot.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.spring.springboot.userservice.dto.UserDTO;
import com.toleyko.spring.springboot.userservice.handlers.GlobalUserHandler;
import com.toleyko.spring.springboot.userservice.handlers.UserPermissionHandler;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handlers.exceptions.UserAlreadyExistException;
import com.toleyko.spring.springboot.userservice.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

@AutoConfigureMockMvc
@EnableWebSecurity
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserPermissionHandler userPermissionHandler;
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
        userController.setUserService(userService);
        userController.setPermissionHandler(userPermissionHandler);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalUserHandler())
                .build();
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

    @Test
    public void getUsers_UnAuthorisedTest() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("User")));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .with(authentication(authentication)))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).getUsers();
    }

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

    @Test
    public void createUser_UserAlreadyExistExceptionTest() throws Exception {
        UserDTO userDTO = new UserDTO("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userService.createUser(userDTO)).thenThrow(new UserAlreadyExistException("msg"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isConflict());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    public void createUser_BadUserDataExceptionTest() throws Exception {
        UserDTO userDTO = new UserDTO("Per","3@d","root", "Kir", "Tol");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("Per");
        when(userService.createUser(userDTO)).thenThrow(new BadUserDataException("msg"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    public void getUser_SuccessfulTest() throws Exception {
        String username = "per";
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        when(userService.getUserByUsername(username)).thenReturn(userRepresentation);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").value(username));
        verify(userService, times(1)).getUserByUsername(username);
    }

    @Test
    public void getUser_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        doReturn(false).when(userPermissionHandler).isPermitted(username);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", username))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).getUserByUsername(username);
    }

    @Test
    public void updateUser_SuccessfulTest() throws Exception {
        String username = "per";
        UserRepresentation userRepresentation = new UserRepresentation();
        UserDTO userDTO = new UserDTO("per", "2@d", "pass", "name", "las");
        userRepresentation.setUsername(username);
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        when(userService.updateUser(username, userDTO)).thenReturn(userRepresentation);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.username").value(username));
        verify(userService, times(1)).updateUser(username, userDTO);
    }

    @Test
    public void updateUser_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        UserDTO userDTO = new UserDTO("per", "2@d", "pass", "name", "las");
        doReturn(false).when(userPermissionHandler).isPermitted(username);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{username}", username)                      .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).updateUser(username, userDTO);
    }

    @Test
    public void deleteUserByUserName_SuccessfulTest() throws Exception {
        String username = "per";
        doReturn(true).when(userPermissionHandler).isPermitted(username);
        doNothing().when(userService).deleteUser(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", username))
                .andExpect(status().isOk());
        verify(userService, times(1)).deleteUser(username);
    }

    @Test
    public void deleteUserByUserName_ForbiddenExceptionTest() throws Exception {
        String username = "per";
        doReturn(false).when(userPermissionHandler).isPermitted(username);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", username))
                .andExpect(status().isForbidden());
        verify(userService, times(0)).deleteUser(username);
    }

}
