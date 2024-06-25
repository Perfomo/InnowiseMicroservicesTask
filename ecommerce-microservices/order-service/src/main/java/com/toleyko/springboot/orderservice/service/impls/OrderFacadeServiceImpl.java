package com.toleyko.springboot.orderservice.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.UserInfoHandler;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import com.toleyko.springboot.orderservice.service.OrderFacadeService;
import com.toleyko.springboot.orderservice.service.OrderService;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderFacadeServiceImpl implements OrderFacadeService {
    private KafkaToInventoryMessagePublisher publisher;
    private OrderService orderService;
    private UserInfoHandler userInfoHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Order saveAndSendOrder(Order order) throws TokenDataExtractionException, JsonProcessingException {
        Order savedOrder = orderService.saveOrder(order.setUserId(userInfoHandler.getUserId()));
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(savedOrder));
        return savedOrder;
    }
}
