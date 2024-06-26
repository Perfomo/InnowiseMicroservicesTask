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
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InventoryControllerTest {
    @Mock
    private InventoryServiceImpl inventoryService;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InventoryController inventoryController = new InventoryController(inventoryService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(inventoryController)
                .setControllerAdvice(GlobalInventoryHandler.class)
                .build();
    }

    @Test
    public void getAllRemainders_SuccessfulTest() throws Exception {
        List<Remainder> expected = new ArrayList<>();
        expected.add(new Remainder().setId(1L).setName("pc").setLeft(1).setSold(0));
        expected.add(new Remainder().setId(2L).setName("car").setLeft(1).setSold(0));
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
        Long id = 1L;
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
        Long id = 1L;

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
        remainder.setId(1L);
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
        Long id = 1L;
        String name = "PC";
        Remainder remainder = new Remainder();
        remainder.setId(id).setName(name).setLeft(2).setSold(2).setCost(BigDecimal.valueOf(2.2));
        when(inventoryService.saveRemainder(any(Remainder.class))).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isOk())
                .andReturn();
        verify(inventoryService, times(1)).saveRemainder(any(Remainder.class));
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }
    @Test
    public void saveRemainder_BeanValidationExceptionTest() throws Exception {
        Remainder remainder = new Remainder()
                .setName("")
                .setSold(-1)
                .setLeft(-1)
                .setCost(BigDecimal.ZERO);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("name", "Invalid name");
        expected.put("left", "Left amount must be 0 or more");
        expected.put("sold", "Sold amount must be 0 or more");
        expected.put("cost", "Cost must be more than 0");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        HashMap resultMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        Assertions.assertEquals(expected, resultMap);
    }

    @Test
    public void updateRemainder_SuccessfulTest() throws Exception {
        Long id = 1L;
        String name = "PC";
        Remainder remainder = new Remainder();
        remainder.setId(id);
        remainder.setName(name);
        when(inventoryService.updateRemainderById(eq(id), any(Remainder.class))).thenReturn(remainder);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isOk())
                .andReturn();
        verify(inventoryService, times(1)).updateRemainderById(eq(id), any(Remainder.class));
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(remainder), responseBody);
    }
    @Test
    public void updateRemainder_RemainderNotFoundExceptionTest() throws Exception {
        Remainder remainder = new Remainder().setName("pc");
        Long id = 1L;

        when(inventoryService.updateRemainderById(eq(id), any(Remainder.class))).thenThrow(new RemainderNotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(RemainderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));
        verify(inventoryService, times(1)).updateRemainderById(eq(id), any(Remainder.class));
    }
    @Test
    public void updateRemainder_BeanValidationExceptionTest() throws Exception {
        Remainder remainder = new Remainder()
                .setName("")
                .setSold(-1)
                .setLeft(-1)
                .setCost(BigDecimal.ZERO);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("name", "Invalid name");
        expected.put("left", "Left amount must be 0 or more");
        expected.put("sold", "Sold amount must be 0 or more");
        expected.put("cost", "Cost must be more than 0");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(remainder)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        HashMap resultMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        Assertions.assertEquals(expected, resultMap);
    }

    @Test
    public void updateRemainderLeftAmountById_SuccessfulTest() throws Exception {
        Long id = 1L;
        Integer amount = 5;
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
        Long id = 1L;
        Integer amount = 5;
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
        Long id = 1L;
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
        Long id = 1L;
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
