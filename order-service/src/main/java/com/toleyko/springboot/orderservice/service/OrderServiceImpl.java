package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handlers.exception.OrderNotFoundException;
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
    public void deleteOrderById(Integer id) {
        orderRepository.deleteById(id);
    }
    @Override
    public Order saveOrder(Order order, String userId) {

        return orderRepository.save(order);
    }
    @Override
    public Order updateOrderById(Integer id, Order order) throws OrderNotFoundException {
        Order oldOrder = this.getOrderById(id);
        oldOrder.setStatus(order.getStatus());
        oldOrder.setNames(order.getNames());
        oldOrder.setCost(order.getCost());
        orderRepository.save(oldOrder);
        return oldOrder;
    }

    @Autowired
    public void setRemainderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
