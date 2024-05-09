package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Override
    public Order getOrderById(Integer id) throws OrderNotFoundException {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new OrderNotFoundException("Order not found");
    }
    @Override
    public Order deleteOrderById(Integer id) throws OrderNotFoundException {
        Optional<Order> optional = orderRepository.findById(id);
        System.out.println("Get optional");
        if (optional.isPresent()) {
            orderRepository.deleteById(id);
            System.out.println("deleted");
            return optional.get();
        }
        throw new OrderNotFoundException("Order not found");
    }
    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public Order updateOrderById(Integer id, Order order) throws OrderNotFoundException {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            Order oldOrder = optional.get();
            oldOrder.setStatus(order.getStatus());
            oldOrder.setProducts(order.getProducts());
            oldOrder.setCost(order.getCost());
            return orderRepository.save(oldOrder);
        }
        throw new OrderNotFoundException("Order not found");
    }

    @Override
    public void deleteOrderByUsername(String username) {
        System.out.println("here");
        orderRepository.deleteAllByUsername(username);
    }

    @Override
    public List<Order> getOrdersByUsername(String username) throws OrderNotFoundException {
        List<Order> orderList = orderRepository.getOrderByUsername(username);
        if (orderList.isEmpty()) {
            throw new OrderNotFoundException("User " + username + " doesn't have any orders");
        }
        return orderList;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
