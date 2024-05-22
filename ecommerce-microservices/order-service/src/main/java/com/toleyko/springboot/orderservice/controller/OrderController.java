package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.TokenHandler;
import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderService;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderService orderService;
    private TokenHandler tokenHandler;
    private KafkaToInventoryMessagePublisher publisher;
    private ObjectMapper objectMapper = new ObjectMapper();
    private OrdersHistoryService ordersHistoryService;

    @GetMapping("/history")
    public List<History> getHistory() {
        return ordersHistoryService.getHistory();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders(@RequestHeader("Authorization") String authorizationHeader) throws ForbiddenException, JsonProcessingException {
        if (tokenHandler.isManager(authorizationHeader)) {
            return orderService.getAllOrders();
        }
        throw new ForbiddenException("Access denied");
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Integer id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    @GetMapping("/{username}/orders")
    public List<Order> getUserOrders(@PathVariable String username, @RequestHeader("Authorization") String authorizationHeader) throws JsonProcessingException, ForbiddenException, OrderNotFoundException {
        String authUsername = tokenHandler.getUsername(authorizationHeader);
        if (username.equals(authUsername) || tokenHandler.isManager(authorizationHeader)) {
            return orderService.getOrdersByUsername(username);
        }
        throw new ForbiddenException("Access denied");
    }

    @PostMapping("/orders")
    public Order saveOrder(@Valid @RequestBody Order order, @RequestHeader("Authorization") String authorizationHeader) throws JsonProcessingException {
        String username = tokenHandler.getUsername(authorizationHeader);
        String userId = tokenHandler.getUserId(authorizationHeader);
        order.setUsername(username);
        order.setUserId(userId);
        Order order1 = orderService.saveOrder(order);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(order1));
        return order1;
    }

    @PutMapping("/orders/{id}")
    public Order updateOrder(@PathVariable Integer id, @RequestBody Order order) throws OrderNotFoundException {
        return orderService.updateOrderById(id, order);
    }

    @DeleteMapping("/orders/{id}")
    public Order deleteOrderById(@PathVariable Integer id) throws OrderNotFoundException {
        return orderService.deleteOrderById(id);
    }

    @Autowired
    public void setTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setPublisher(KafkaToInventoryMessagePublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired
    public void setOrdersHistoryService(OrdersHistoryService ordersHistoryService) {
        this.ordersHistoryService = ordersHistoryService;
    }
}
