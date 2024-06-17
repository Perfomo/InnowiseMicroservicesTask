package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.handler.PermissionHandler;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import com.toleyko.springboot.orderservice.service.OrderService;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import com.toleyko.springboot.orderservice.service.kafka.KafkaToInventoryMessagePublisher;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderService orderService;
    private KafkaToInventoryMessagePublisher publisher;
    private ObjectMapper objectMapper = new ObjectMapper();
    private OrdersHistoryService ordersHistoryService;
    private PermissionHandler permissionHandler;

    @GetMapping("/history")
    public List<History> getHistory() {
        return ordersHistoryService.getHistory();
    }

    @GetMapping("/orders")
    public List<Order> getAllOrders() throws ForbiddenException, TokenDataExtractionException {
        if (permissionHandler.isManager()) {
            return orderService.getAllOrders();
        }
        throw new ForbiddenException("Access denied");
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Integer id) throws OrderNotFoundException {
        return orderService.getOrderById(id);
    }

    @GetMapping("/{username}/orders")
    public List<Order> getUserOrders(@PathVariable String username) throws OrderNotFoundException, ForbiddenException, TokenDataExtractionException {
        String authUsername = permissionHandler.getUsername();
        if (username.equals(authUsername) || permissionHandler.isManager()) {
            return orderService.getOrdersByUsername(username);
        }
        throw new ForbiddenException("Access denied");
    }
    @PostMapping("/orders")
    public Order saveOrder(@Valid @RequestBody Order order) throws TokenDataExtractionException, JsonProcessingException {
        String username = permissionHandler.getUsername();
        String userId = permissionHandler.getUserId();
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

    @Autowired
    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }
}
