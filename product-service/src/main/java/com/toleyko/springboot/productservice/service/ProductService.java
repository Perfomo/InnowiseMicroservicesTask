package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Integer id);
    public void deleteProductById(Integer id);
    public void saveProduct(Product product);
}
