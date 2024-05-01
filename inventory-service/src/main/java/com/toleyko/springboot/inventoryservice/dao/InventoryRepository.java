package com.toleyko.springboot.inventoryservice.dao;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Remainder, Integer> {

}
