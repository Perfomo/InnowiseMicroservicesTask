package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.GlobalOrderHandler;
import com.toleyko.springboot.orderservice.handler.OrderStatus;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import com.toleyko.springboot.orderservice.service.OrderFacadeService;
import com.toleyko.springboot.orderservice.service.impls.OrderServiceImpl;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private OrderFacadeService orderFacadeService;
    @Mock
    private OrdersHistoryService ordersHistoryService;
    private OrderController orderController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderController = new OrderController(orderService, ordersHistoryService, orderFacadeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .setControllerAdvice(new GlobalOrderHandler())
                .build();
    }

    @Test
    public void getHistory_SuccessfulTest() throws Exception {
        List<History> expected = new ArrayList<>();
        expected.add(new History().setId(1L));
        expected.add(new History().setId(2L));
        when(ordersHistoryService.getHistory()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(ordersHistoryService, times(1)).getHistory();
    }

    @Test
    public void getAllOrders_SuccessfulTest() throws Exception {
        List<Order> expected = new ArrayList<>();
        expected.add(new Order().setStatus(OrderStatus.OK).setCost(BigDecimal.valueOf(2.3)));
        expected.add(new Order().setStatus(OrderStatus.PENDING).setCost(BigDecimal.valueOf(10.0)));
        when(orderService.getAllOrders()).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(OrderStatus.OK.name()))
                .andExpect(jsonPath("$[0].cost").value(BigDecimal.valueOf(2.3)))
                .andExpect(jsonPath("$[1].status").value(OrderStatus.PENDING.name()))
                .andExpect(jsonPath("$[1].cost").value(BigDecimal.valueOf(10.0)));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void getOrderById_SuccessfulTest() throws Exception {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        order.setCost(BigDecimal.valueOf(5.3));
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
        Long id = 1L;
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
        Order order1 = new Order().setId(1L);
        Order order2 = new Order().setId(2L);
        expected.add(order1);
        expected.add(order2);
        when(orderService.getOrdersByUsername(anyString())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }
    @Test
    public void getUserOrders_SuccessfulTest2() throws Exception {
        String username = "notus";
        List<Order> expected = new ArrayList<>();
        Order order1 = new Order().setId(1L);
        Order order2 = new Order().setId(2L);
        expected.add(order1);
        expected.add(order2);
        when(orderService.getOrdersByUsername(anyString())).thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(status().isOk());

        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }
    @Test
    public void getUserOrders_OrderNotFoundExceptionTest() throws Exception {
        String username = "us";
        when(orderService.getOrdersByUsername(anyString())).thenThrow(new OrderNotFoundException("Not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/{username}/orders", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(orderService, times(1)).getOrdersByUsername(anyString());
    }

    @Test
    public void saveOrder_SuccessfulTest() throws Exception {
        Authentication authentication2 = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));

        Order order = new Order();
        order.setCost(BigDecimal.valueOf(1.2));
        order.setUserId("1");
        order.setUsername("user");
        Map<String, String> products = new HashMap<>();
        products.put("2","2");
        order.setProducts(products);
        when(orderFacadeService.saveAndSendOrder(any(Order.class))).thenReturn(order);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authentication2)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andReturn();

        verify(orderFacadeService, times(1)).saveAndSendOrder(any(Order.class));
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }

    @Test
    public void saveOrder_TokenDataExtractionExceptionTest() throws Exception {
        Authentication authentication2 = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));

        Order order = new Order();
        order.setCost(BigDecimal.valueOf(1.2));
        order.setUserId("1");
        order.setUsername("user");
        Map<String, String> products = new HashMap<>();
        products.put("2","2");
        order.setProducts(products);
        when(orderFacadeService.saveAndSendOrder(any(Order.class))).thenThrow(new TokenDataExtractionException("error"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authentication2)
                        .content(asJsonString(order)))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> Assertions.assertInstanceOf(TokenDataExtractionException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("error", result.getResolvedException().getMessage()));
        verify(orderFacadeService, times(1)).saveAndSendOrder(any(Order.class));
    }

    @Test
    public void saveOrder_JsonProcessingExceptionTest() throws Exception {
        Authentication authentication2 = new UsernamePasswordAuthenticationToken("manager", "manager",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));

        Order order = new Order();
        order.setCost(BigDecimal.valueOf(1.2));
        order.setUserId("1");
        order.setUsername("user");
        Map<String, String> products = new HashMap<>();
        products.put("2","2");
        order.setProducts(products);
        when(orderFacadeService.saveAndSendOrder(any(Order.class))).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(authentication2)
                        .content(asJsonString(order)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertInstanceOf(JsonProcessingException.class, result.getResolvedException()));
        verify(orderFacadeService, times(1)).saveAndSendOrder(any(Order.class));
    }

    @Test
    public void saveOrder_BeanValidationExceptionTest() throws Exception {
        Order order = new Order();
        order.setStatus(OrderStatus.ERROR);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("products", "Products list is empty");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        HashMap<String, String> resultMap = new ObjectMapper().readValue(responseBody, HashMap.class);
        Assertions.assertEquals(expected, resultMap);
    }

    @Test
    public void updateOrder_SuccessfulTest() throws Exception {
        Long id = 1L;
        Order order = new Order();
        order.setCost(BigDecimal.valueOf(3.3));
        when(orderService.updateOrderById(anyLong(), any(Order.class))).thenReturn(order);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andReturn();
        verify(orderService, times(1)).updateOrderById(anyLong(), any(Order.class));
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(asJsonString(order), responseBody);
    }
    @Test
    public void updateOrder_OrderNotFoundExceptionTest() throws Exception {
        Long id = 1L;
        Order order = new Order();
        order.setCost(BigDecimal.valueOf(3.3));
        when(orderService.updateOrderById(anyLong(), any(Order.class))).thenThrow(new OrderNotFoundException("Not found"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertInstanceOf(OrderNotFoundException.class, result.getResolvedException()))
                .andExpect(result -> Assertions.assertEquals("Not found", result.getResolvedException().getMessage()));

        verify(orderService, times(1)).updateOrderById(anyLong(), any(Order.class));
    }

    @Test
    public void deleteOrderById_SuccessfulTest() throws Exception {
        Long id = 1L;
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
        Long id = 1L;
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
