package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.UserInfoHandler;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import com.toleyko.springboot.orderservice.service.impls.OrderFacadeServiceImpl;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

public class OrderFacadeServiceImplTest {
    @Mock
    private KafkaToInventoryMessagePublisher publisher;
    @Mock
    private OrderService orderService;
    @Mock
    private UserInfoHandler userInfoHandler;
    private OrderFacadeService orderFacadeService;
    private final Order order = new Order().setUsername("Per");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderFacadeService = new OrderFacadeServiceImpl(publisher, orderService, userInfoHandler);
    }

    @Test
    public void saveAndSendOrder_SuccessfulTest() throws TokenDataExtractionException, JsonProcessingException {
        when(userInfoHandler.getUserId()).thenReturn("id");
        when(orderService.saveOrder(any(Order.class))).thenReturn(order.setUserId("id"));
        doNothing().when(publisher).sendMessageToTopic(anyString());
        Assertions.assertEquals(order.getUserId(), orderFacadeService.saveAndSendOrder(order).getUserId());
    }
    @Test
    public void saveAndSendOrder_TokenDataExtractionExceptionTest() throws TokenDataExtractionException {
        when(userInfoHandler.getUserId()).thenThrow(new TokenDataExtractionException("error"));
        Assertions.assertThrows(TokenDataExtractionException.class, () -> orderFacadeService.saveAndSendOrder(order));
    }
    @Test
    public void saveAndSendOrder_JsonProcessingExceptionTest() throws TokenDataExtractionException, JsonProcessingException {
        when(userInfoHandler.getUserId()).thenReturn("id");
        when(orderService.saveOrder(any(Order.class))).thenReturn(order.setUserId("id"));
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(orderFacadeService, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        Assertions.assertThrows(JsonProcessingException.class, () -> orderFacadeService.saveAndSendOrder(order));
    }
}
