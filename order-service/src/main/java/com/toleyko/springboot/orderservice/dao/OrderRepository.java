package com.toleyko.springboot.orderservice.dao;

import com.toleyko.springboot.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> getOrderByUsername(String username);
    public void deleteAllByUsername(String username);
}
