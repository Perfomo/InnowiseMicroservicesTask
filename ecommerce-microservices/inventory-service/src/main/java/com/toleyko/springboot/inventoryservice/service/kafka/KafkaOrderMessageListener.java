package com.toleyko.springboot.inventoryservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.inventoryservice.dto.Order;
import com.toleyko.springboot.inventoryservice.handlers.OrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderMessageListener {
    private OrderHandler orderHandler;
    private KafkaToOrderMessagePublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "sended-orders", groupId = "sended-orders-group-1")
    public void consume(String message) throws JsonProcessingException {
        System.out.println("Lis rec order");
        System.out.println(message);
        Order order = objectMapper.readValue(message, Order.class);
        System.out.println("old order: " + order);
        Order newOrder = orderHandler.handleOrder(order);
        System.out.println("new order: " + newOrder);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(newOrder));
    }

    @Autowired
    public void setOrderHandler(OrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    @Autowired
    public void setPublisher(KafkaToOrderMessagePublisher publisher) {
        this.publisher = publisher;
    }
}
