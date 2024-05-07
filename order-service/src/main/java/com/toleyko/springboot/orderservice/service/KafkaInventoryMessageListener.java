package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaInventoryMessageListener {
    private OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "handled-orders", groupId = "handled-orders-group-1")
    public void consume(String message) throws JsonProcessingException, OrderNotFoundException {
        System.out.println(message);
        Order order = objectMapper.readValue(message, Order.class);
        orderService.updateOrderById(order.getId(), order);
        System.out.println("Order: " + order + " saved");
    }

    @Autowired
    public void setInventoryService(OrderService orderService) {
        this.orderService = orderService;
    }
}
