package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Integer id) throws ProductNotFoundException;
    public void deleteProductById(Integer id);
    public Product saveProduct(Product product);
}
