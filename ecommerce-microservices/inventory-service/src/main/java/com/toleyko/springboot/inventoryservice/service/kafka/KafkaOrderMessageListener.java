package com.toleyko.springboot.inventoryservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.dto.OrderDto;
import com.toleyko.springboot.inventoryservice.handlers.OrderHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class KafkaOrderMessageListener {
    private OrderHandler orderHandler;
    private KafkaToOrderMessagePublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "sended-orders", groupId = "sended-orders-group-1")
    public void consume(String message) {
        try {
            log.info(message);
            OrderDto order = objectMapper.readValue(message, OrderDto.class);
            log.info("old order: " + order);
            OrderDto newOrder = orderHandler.handleOrder(order);
            log.info("new order: " + newOrder);
            publisher.sendMessageToTopic(objectMapper.writeValueAsString(newOrder));
        }
        catch (Exception e) {
            log.error("KafkaOrderMessageListener -> " + e.getMessage());
        }
    }
}
