package com.toleyko.springboot.inventoryservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.GlobalInventoryHandler;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

public class InventoryControllerTest {
    @Mock
    private InventoryServiceImpl inventoryService;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InventoryController inventoryController = new InventoryController();
        inventoryController.setInventoryService(inventoryService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(inventoryController)
                .setControllerAdvice(GlobalInventoryHandler.class)
                .build();
    }

    @Test
    public void getAllRemainders_SuccessfulTest() throws Exception {
        List<Remainder> expected = new ArrayList<>();
        expected.add(new Remainder().setId(1).setName("pc").setLeft(1).setSold(0));
        expected.add(new Remainder().setId(2).setName("car").setLeft(1).setSold(0));
        when(inventoryService.getAllRemainders()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("pc"))
                .andExpect(jsonPath("$[0].left").value(1))
                .andExpect(jsonPath("$[0].sold").value(0))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(inventoryService, times(1)).getAllRemainders();
    }

    @Test
    public void getRemainderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        Remainder remainder = new Remainder();
        remainder.setId(id);
        remainder.setName("pc");
        when(inventoryService.getRemainderById(id)).thenReturn(remainder);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("pc"));

        verify(inventoryService, times(1)).getRemainderById(id);
    }

    @Test
    public void getRemainderById_RemainderNotFoundExceptionTest() throws Exception {
        Integer id = 1;

        when(inventoryService.getRemainderById(id)).thenThrow(RemainderNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inventoryService, times(1)).getRemainderById(id);
    }

    @Test
    public void saveRemainder_SuccessfulTest() throws Exception {
        Integer id = 1;
        String name = "PC";
        Remainder remainder = new Remainder();
        remainder.setId(id).setName(name);
        when(inventoryService.saveRemainder(remainder)).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isOk())
                .andReturn();
        verify(inventoryService, times(1)).saveRemainder(remainder);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }

    @Test
    public void updateRemainder_SuccessfulTest() throws Exception {
        Integer id = 1;
        String name = "PC";
        Remainder remainder = new Remainder();
        remainder.setId(id);
        remainder.setName(name);
        when(inventoryService.updateRemainderById(id, remainder)).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isOk())
                .andReturn();
        verify(inventoryService, times(1)).updateRemainderById(id, remainder);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }
    @Test
    public void updateRemainder_RemainderNotFoundExceptionTest() throws Exception {
        Remainder remainder = new Remainder().setName("pc");
        Integer id = 1;

        when(inventoryService.updateRemainderById(id, remainder)).thenThrow(RemainderNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isNotFound());
        verify(inventoryService, times(1)).updateRemainderById(id, remainder);
    }

    @Test
    public void deleteRemainderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        doNothing().when(inventoryService).deleteRemainderById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/inventory/{id}", id))
                .andExpect(status().isOk());
        verify(inventoryService, times(1)).deleteRemainderById(id);
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
