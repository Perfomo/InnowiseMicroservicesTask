package com.toleyko.springboot.orderservice.dao;

import com.toleyko.springboot.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
