package com.toleyko.springboot.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.OrderStatus;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
import com.toleyko.springboot.orderservice.service.OrderFacadeService;
import com.toleyko.springboot.orderservice.service.OrderService;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private OrderService orderService;
    private OrdersHistoryService ordersHistoryService;
    private OrderFacadeService orderFacadeService;

    @GetMapping("/history")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<History>> getHistory() {
        return ResponseEntity.ok(ordersHistoryService.getHistory());
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/{username}/orders")
    @PreAuthorize("authentication.name == #username || hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String username) throws OrderNotFoundException {
        return ResponseEntity.ok(orderService.getOrdersByUsername(username));
    }

    @PostMapping("/orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> saveOrder(@Valid @RequestBody Order order, Principal principal) throws TokenDataExtractionException, JsonProcessingException {
        return ResponseEntity.ok(orderFacadeService.saveAndSendOrder(order.setUsername(principal.getName()).setStatus(OrderStatus.PENDING)));
    }

    @PutMapping("/orders/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) throws OrderNotFoundException {
        return ResponseEntity.ok(orderService.updateOrderById(id, order));
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<Order> deleteOrderById(@PathVariable Long id) throws OrderNotFoundException {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }
}
