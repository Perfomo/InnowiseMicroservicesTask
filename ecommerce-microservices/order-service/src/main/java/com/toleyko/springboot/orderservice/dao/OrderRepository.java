package com.toleyko.springboot.orderservice.dao;

import com.toleyko.springboot.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> getOrderByUsername(String username);
    @Transactional
    public void deleteAllByUsername(String username);
}
