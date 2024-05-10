package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;

import java.util.List;
public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Integer id) throws ProductNotFoundException;
    public Product getProductByName(String name) throws ProductNotFoundException;
    public Product deleteProductById(Integer id) throws ProductNotFoundException;
    public Product saveProduct(Product product);
    public Product updateProductById(Integer id, Product product) throws ProductNotFoundException;
}
