package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;

import java.util.List;

public interface OrderService {
    public List<Order> getAllOrders();
    public Order getOrderById(Integer id) throws OrderNotFoundException;
    public Order deleteOrderById(Integer id) throws OrderNotFoundException;
    public Order saveOrder(Order order);
    public Order updateOrderById(Integer id, Order order) throws OrderNotFoundException;
    public List<Order> getOrdersByUsername(String username) throws OrderNotFoundException;
    public void deleteOrderByUsername(String username);
}
