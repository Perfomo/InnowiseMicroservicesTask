package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.GlobalOrderHandler;
import com.toleyko.springboot.orderservice.handler.GlobalOrderHandlerTest;
import com.toleyko.springboot.orderservice.handler.TokenHandler;
import static org.mockito.Mockito.*;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
public class OrderControllerTest {
    @Mock
    private TokenHandler tokenHandler;
    @Mock
    private OrderServiceImpl orderService;
    private OrderController orderController = new OrderController();
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderController.setOrderService(orderService);
        orderController.setTokenHandler(tokenHandler);
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new GlobalOrderHandler())
                .build();
    }

    @Test
    public void getAllOrders_SuccessfulTest() throws Exception {
        List<Order> expected = new ArrayList<>();
        expected.add(new Order().setStatus("ok").setCost(2.3));
        expected.add(new Order().setStatus("p").setCost(10.0));
        when(tokenHandler.isManager(anyString())).thenReturn(true);
        when(orderService.getAllOrders()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ok"))
                .andExpect(jsonPath("$[0].cost").value(2.3))
                .andExpect(jsonPath("$[1].status").value("p"))
                .andExpect(jsonPath("$[1].cost").value(10.0));
        verify(tokenHandler, times(1)).isManager(anyString());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void getAllOrders_ForbiddenExceptionTest() throws Exception {
        when(tokenHandler.isManager(anyString())).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere"))
                .andExpect(status().isForbidden());

        verify(tokenHandler, times(1)).isManager(anyString());
        verify(orderService, times(0)).getAllOrders();
    }

    @Test
    public void getAllOrders_JsonProcessingExceptionTest() throws Exception {
        when(tokenHandler.isManager(anyString())).thenThrow(JsonProcessingException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere"))
                .andExpect(status().isBadRequest());

        verify(tokenHandler, times(1)).isManager(anyString());
        verify(orderService, times(0)).getAllOrders();
    }

    @Test
    public void getOrderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        Order order = new Order();
        order.setId(id);
        order.setCost(5.3);
        when(orderService.getOrderById(id)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.cost").value(5.3));
        verify(orderService, times(1)).getOrderById(id);
    }

    @Test
    public void getOrderById_OrderNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(orderService.getOrderById(id)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(orderService, times(1)).getOrderById(id);
    }

    @Test
    /// rewiew
    public void saveOrder_SuccessfulTest() throws Exception {
        Order order = new Order();
        order.setCost(1.2);
        when(tokenHandler.getUsername(anyString())).thenReturn("user");
        when(orderService.saveOrder(order)).thenReturn(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andReturn();
        verify(orderService, times(1)).saveOrder(order);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }

    @Test
    ///rewiew
    public void saveOrder_JsonProcessingExceptionTest() throws Exception {
        Order order = new Order();
        order.setCost(1.2);
        when(tokenHandler.getUsername(anyString())).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isBadRequest());
        verify(orderService, times(0)).saveOrder(order);
    }

    @Test
    public void updateOrder_SuccessfulTest() throws Exception {
        Integer id = 1;
        Order order = new Order();
        order.setCost(3.3);
        when(orderService.updateOrderById(id, order)).thenReturn(order);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andReturn();
        verify(orderService, times(1)).updateOrderById(id, order);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }

    @Test
    public void updateOrder_OrderNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        Order order = new Order();
        order.setCost(3.3);
        when(orderService.updateOrderById(id, order)).thenThrow(OrderNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isNotFound());
        verify(orderService, times(1)).updateOrderById(id, order);
    }

    @Test
    public void deleteOrderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        doNothing().when(orderService).deleteOrderById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", id))
                .andExpect(status().isOk());
        verify(orderService, times(1)).deleteOrderById(id);
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
