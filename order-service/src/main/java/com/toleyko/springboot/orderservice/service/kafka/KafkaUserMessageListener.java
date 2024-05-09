package com.toleyko.springboot.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaUserMessageListener {
    private OrderService orderService;
    @KafkaListener(topics = "deleted-users-orders", groupId = "deleted-users-orders-group-1")
    public void consume(String message) {
        System.out.println(message);
        orderService.deleteOrderByUsername(message);
        System.out.println(message + "'s orders deleted");
    }

    @Autowired
    public void setInventoryService(OrderService orderService) {
        this.orderService = orderService;
    }
}
