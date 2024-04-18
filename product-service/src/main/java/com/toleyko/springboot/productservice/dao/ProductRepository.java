package com.toleyko.springboot.productservice.dao;

import com.toleyko.springboot.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Integer> {
}
