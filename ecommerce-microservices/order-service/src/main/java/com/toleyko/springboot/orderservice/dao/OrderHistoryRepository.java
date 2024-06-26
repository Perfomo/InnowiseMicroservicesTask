package com.toleyko.springboot.orderservice.dao;

import com.toleyko.springboot.orderservice.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<History, Long> {
}
