package com.toleyko.springboot.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.KafkaMessagePublisher;
import com.toleyko.springboot.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    private KafkaMessagePublisher publisher;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Integer id) throws ProductNotFoundException {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public Product saveProduct(@RequestBody Product product) throws JsonProcessingException {
        Product product1 = productService.saveProduct(product);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"save", product1.getName()}));
        return product1;
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) throws ProductNotFoundException, JsonProcessingException {
        String oldName = productService.getProductById(id).getName();
        Product product1 = productService.updateProductById(id, product);
        if (!oldName.equals(product.getName())) {
            publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"update", oldName +"~"+ product1.getName()}));
        }
        return product1;
    }

    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable Integer id) throws JsonProcessingException, ProductNotFoundException {
        String name = productService.getProductById(id).getName();
        productService.deleteProductById(id);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"delete", name}));
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setPublisher(KafkaMessagePublisher publisher) {
        this.publisher = publisher;
    }
}
