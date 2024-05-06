package com.toleyko.springboot.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderMessageListener {
    private InventoryServiceImpl inventoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "sended-orders", groupId = "sended-orders-group-1")
    public void consume(String message) throws JsonProcessingException {
        Order order = objectMapper.readValue(message, Order.class);

    }

    @Autowired
    public void setInventoryService(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }
}
