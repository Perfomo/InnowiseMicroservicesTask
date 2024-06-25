package com.toleyko.springboot.orderservice.service.impls;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Override
    public Order getOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
    @Override
    public Order deleteOrderById(Long id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        orderRepository.deleteById(id);
        return order;
    }
    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    @Override
    public Order updateOrderById(Long id, Order order) throws OrderNotFoundException {
        Order oldOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        oldOrder.setStatus(order.getStatus());
        oldOrder.setProducts(order.getProducts());
        oldOrder.setCost(order.getCost());
        return orderRepository.save(oldOrder);
    }

    @Override
    public void deleteOrderByUsername(String username) {
        orderRepository.deleteAllByUsername(username);
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.getOrderByUsername(username);
    }
}
