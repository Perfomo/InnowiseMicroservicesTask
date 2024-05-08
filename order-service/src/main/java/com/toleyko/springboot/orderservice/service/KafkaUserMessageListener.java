package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaUserMessageListener {
    private OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "deleted-users-orders", groupId = "deleted-users-orders-group-1")
    public void consume(String message) throws JsonProcessingException, OrderNotFoundException {
        String username = objectMapper.readValue(message, String.class);
        orderService.deleteOrderByUsername(username);
        System.out.println(username + " user orders deleted");
    }

    @Autowired
    public void setInventoryService(OrderService orderService) {
        this.orderService = orderService;
    }
}
