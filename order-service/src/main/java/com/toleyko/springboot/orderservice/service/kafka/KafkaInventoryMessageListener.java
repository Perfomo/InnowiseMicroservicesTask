package com.toleyko.springboot.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderService;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaInventoryMessageListener {
    private OrderService orderService;
    private OrdersHistoryService ordersHistoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "handled-orders", groupId = "handled-orders-group-1")
    public void consume(String message) throws JsonProcessingException, OrderNotFoundException {
        System.out.println("Leist rec");
        System.out.println(message);
        Order order = objectMapper.readValue(message, Order.class);
        orderService.updateOrderById(order.getId(), order);
        ordersHistoryService.save(order);
        System.out.println("Order: " + order + " saved");
    }

    @Autowired
    public void setInventoryService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrdersHistoryService(OrdersHistoryService ordersHistoryService) {
        this.ordersHistoryService = ordersHistoryService;
    }
}
