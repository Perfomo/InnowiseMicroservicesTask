package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handlers.TokenHandler;
import com.toleyko.springboot.orderservice.handlers.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handlers.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    private OrderService orderService;
    private TokenHandler tokenHandler;

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

    @PostMapping("/orders")
    public Order saveOrder(@RequestBody Order order, @RequestHeader("Authorization") String authorizationHeader) throws JsonProcessingException {
        String userId = tokenHandler.getUserId(authorizationHeader);
        return orderService.saveOrder(order, userId);
    }

    @PutMapping("/orders/{id}")
    public Order updateOrder(@PathVariable Integer id, @RequestBody Order order) throws OrderNotFoundException {
        return orderService.updateOrderById(id, order);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrderById(@PathVariable Integer id) {
        orderService.deleteOrderById(id);
    }

    @Autowired
    public void setTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
