package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.GlobalOrderHandler;
import com.toleyko.springboot.orderservice.handler.GlobalOrderHandlerTest;
import com.toleyko.springboot.orderservice.handler.TokenHandler;
import static org.mockito.Mockito.*;

import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderServiceImpl;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
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
    @Mock
    private KafkaToInventoryMessagePublisher publisher;
    private OrderController orderController = new OrderController();
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderController.setOrderService(orderService);
        orderController.setTokenHandler(tokenHandler);
        orderController.setPublisher(publisher);
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
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Access denied", result.getResolvedException().getMessage()));

        verify(tokenHandler, times(1)).isManager(anyString());
        verify(orderService, times(0)).getAllOrders();
    }

    @Test
    public void getAllOrders_JsonProcessingExceptionTest() throws Exception {
        when(tokenHandler.isManager(anyString())).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(JsonProcessingException.class, result.getResolvedException()));

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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.cost").value(5.3))
                .andReturn();

        verify(orderService, times(1)).getOrderById(id);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }

    @Test
    public void getOrderById_OrderNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(orderService.getOrderById(id)).thenThrow(new OrderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(orderService, times(1)).getOrderById(id);
    }

    @Test
    public void getUserOrders_SuccessfulTest1() throws Exception {
        String username = "us";
        List<Order> expected = new ArrayList<>();
        Order order1 = new Order().setId(1);
        Order order2 = new Order().setId(2);
        expected.add(order1);
        expected.add(order2);
        when(tokenHandler.getUsername(anyString())).thenReturn("us");
        when(orderService.getOrdersByUsername(anyString())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, anyString()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(status().isOk());

        verify(tokenHandler, times(1)).getUsername(anyString());
        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }
    @Test
    public void getUserOrders_SuccessfulTest2() throws Exception {
        String username = "notus";
        List<Order> expected = new ArrayList<>();
        Order order1 = new Order().setId(1);
        Order order2 = new Order().setId(2);
        expected.add(order1);
        expected.add(order2);
        when(tokenHandler.getUsername(anyString())).thenReturn("us");
        when(tokenHandler.isManager(anyString())).thenReturn(true);
        when(orderService.getOrdersByUsername(anyString())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, anyString()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(status().isOk());

        verify(tokenHandler, times(1)).getUsername(anyString());
        verify(tokenHandler, times(1)).isManager(anyString());
        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }
    @Test
    public void getUserOrders_JsonProcessingExceptionTest() throws Exception {
        String username = "notus";
        when(tokenHandler.getUsername(anyString())).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, anyString()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(JsonProcessingException.class, result.getResolvedException()));

        verify(tokenHandler, times(1)).getUsername(anyString());
    }
    @Test
    public void getUserOrders_ForbiddenExceptionTest() throws Exception {
        String username = "notus";
        when(tokenHandler.getUsername(anyString())).thenReturn("us");
        when(tokenHandler.isManager(anyString())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, anyString()))
                .andExpect(status().isForbidden())
                .andExpect(result -> Assertions.assertInstanceOf(ForbiddenException.class, result.getResolvedException()));

        verify(tokenHandler, times(1)).getUsername(anyString());
        verify(tokenHandler, times(1)).isManager(anyString());
    }
    @Test
    public void getUserOrders_OrderNotFoundExceptionTest() throws Exception {
        String username = "us";
        when(tokenHandler.getUsername(anyString())).thenReturn("us");
        when(orderService.getOrdersByUsername(anyString())).thenThrow(new OrderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, anyString()))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(tokenHandler, times(1)).getUsername(anyString());
        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }

    @Test
    public void saveOrder_SuccessfulTest() throws Exception {
        Order order = new Order();
        order.setCost(1.2);
        order.setUserId("1");
        order.setUsername("user");
        when(tokenHandler.getUsername(anyString())).thenReturn("user");
        when(tokenHandler.getUserId(anyString())).thenReturn(String.valueOf(1));
        when(orderService.saveOrder(order)).thenReturn(order);
        doNothing().when(publisher).sendMessageToTopic(anyString());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andReturn();

        verify(orderService, times(1)).saveOrder(order);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }
    @Test
    public void saveOrder_JsonProcessingExceptionTest() throws Exception {
        Order order = new Order();
        order.setCost(1.2);
        when(tokenHandler.getUsername(anyString())).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer yourAuthorizationTokenHere")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(JsonProcessingException.class, result.getResolvedException()));

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
        when(orderService.updateOrderById(id, order)).thenThrow(new OrderNotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(orderService, times(1)).updateOrderById(id, order);
    }

    @Test
    public void deleteOrderById_SuccessfulTest() throws Exception {
        Integer id = 1;
        Order order = new Order().setId(id);
        when(orderService.deleteOrderById(id)).thenReturn(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
        verify(orderService, times(1)).deleteOrderById(id);
    }
    @Test
    public void deleteOrderById_OrderNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(orderService.deleteOrderById(id)).thenThrow(new OrderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(orderService, times(1)).deleteOrderById(id);
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
