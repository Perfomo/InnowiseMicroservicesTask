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
import java.util.Objects;

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("pc"))
                .andReturn();

        verify(inventoryService, times(1)).getRemainderById(id);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }

    @Test
    public void getRemainderById_RemainderNotFoundExceptionTest() throws Exception {
        Integer id = 1;

        when(inventoryService.getRemainderById(id)).thenThrow(new RemainderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(inventoryService, times(1)).getRemainderById(id);
    }

    @Test
    public void getRemainderByName_SuccessfulTest() throws Exception {
        String name = "pc";
        Remainder remainder = new Remainder();
        remainder.setId(1);
        remainder.setName(name);
        when(inventoryService.getRemainderByName(name)).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(name))
                .andReturn();

        verify(inventoryService, times(1)).getRemainderByName(name);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }

    @Test
    public void getRemainderByName_RemainderNotFoundExceptionTest() throws Exception {
        String name = "pc";

        when(inventoryService.getRemainderByName(name)).thenThrow(new RemainderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/name/{name}", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(inventoryService, times(1)).getRemainderByName(name);
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

        when(inventoryService.updateRemainderById(id, remainder)).thenThrow(new RemainderNotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));
        verify(inventoryService, times(1)).updateRemainderById(id, remainder);
    }

    @Test
    public void updateRemainderLeftAmountById_SuccessfulTest() throws Exception {
        Integer id = 1, amount = 5;
        Remainder remainder = new Remainder().setId(id).setLeft(amount);
        when(inventoryService.updateRemainderLeftAmount(id, amount)).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}/amount", id)
                .param("amount", amount.toString()))
                .andExpect(status().isOk())
                .andReturn();

        verify(inventoryService, times(1)).updateRemainderLeftAmount(id, amount);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }
    @Test
    public void updateRemainderLeftAmountById_RemainderNotFoundExceptionTest() throws Exception {
        Integer id = 1, amount = 5;
        when(inventoryService.updateRemainderLeftAmount(id, amount)).thenThrow(new RemainderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}/amount", id)
                        .param("amount", amount.toString()))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(inventoryService, times(1)).updateRemainderLeftAmount(id, amount);
    }

    @Test
    public void deleteRemainderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        Remainder remainder = new Remainder().setId(id);
        when(inventoryService.deleteRemainderById(id)).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(inventoryService, times(1)).deleteRemainderById(id);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }
    @Test
    public void deleteRemainderById_RemainderNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(inventoryService.deleteRemainderById(id)).thenThrow(new RemainderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));
        verify(inventoryService, times(1)).deleteRemainderById(id);
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
