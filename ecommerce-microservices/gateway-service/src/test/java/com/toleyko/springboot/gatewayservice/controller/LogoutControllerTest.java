package com.toleyko.springboot.gatewayservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LogoutControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        LogoutController logoutController = new LogoutController();
        mockMvc = MockMvcBuilders
                .standaloneSetup(logoutController)
                .build();
    }

    @Test
    public void showHomepageTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/homepage"))
                .andExpect(status().isOk());
    }

    @Test
    public void logoutTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/logout"))
                .andExpect(status().is(302));
    }
}
