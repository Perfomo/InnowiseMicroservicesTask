package com.toleyko.springboot.inventoryservice.dao;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Remainder, Integer> {
    public Optional<Remainder> findByName(String name);
}
