package com.toleyko.springboot.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;

public interface ProductFacadeService {
    public Product saveAndSendProduct(Product product) throws JsonProcessingException;
    public Product updateAndSendProduct(Product product, Long id) throws ProductNotFoundException, JsonProcessingException;
    public Product deleteAndSendProduct(Long id) throws ProductNotFoundException, JsonProcessingException;
}
