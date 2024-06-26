package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;

import java.util.List;
public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Long id) throws ProductNotFoundException;
    public Product getProductByName(String name) throws ProductNotFoundException;
    public Product deleteProductById(Long id) throws ProductNotFoundException;
    public Product saveProduct(Product product);
    public Product updateProductById(Long id, Product product) throws ProductNotFoundException;
}
