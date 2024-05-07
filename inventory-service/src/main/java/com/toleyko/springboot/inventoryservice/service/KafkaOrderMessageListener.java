package com.toleyko.springboot.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderMessageListener {
    private InventoryServiceImpl inventoryService;
    private KafkaToOrderMessagePublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "sended-orders", groupId = "sended-orders-group-1")
    public void consume(String message) throws JsonProcessingException {
        System.out.println(message);
        Order order = objectMapper.readValue(message, Order.class);
        System.out.println("old order: " + order);
        Order newOrder = inventoryService.handleOrder(order);
        System.out.println("new order: " + newOrder);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(newOrder));
    }

    @Autowired
    public void setInventoryService(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Autowired
    public void setPublisher(KafkaToOrderMessagePublisher publisher) {
        this.publisher = publisher;
    }
}
