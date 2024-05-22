package com.toleyko.springboot.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.KafkaToInventoryMessagePublisher;
import com.toleyko.springboot.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;
    private KafkaToInventoryMessagePublisher publisher;
    private ObjectMapper objectMapper = new ObjectMapper();
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Integer id) throws ProductNotFoundException {
        return productService.getProductById(id);
    }

    @GetMapping("/products/name/{name}")
    public Product getProductByName(@PathVariable String name) throws ProductNotFoundException {
        return productService.getProductByName(name);
    }

    @PostMapping("/products")
    public Product saveProduct(@Valid @RequestBody Product product) throws JsonProcessingException {
        Product res = productService.saveProduct(product);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"save", res.getName(), res.getCost().toString()}));
        return res;
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product) throws ProductNotFoundException, JsonProcessingException {
        String oldName = productService.getProductById(id).getName();
        Product res = productService.updateProductById(id, product);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"update", oldName +"~"+ res.getName(), res.getCost().toString()}));
        return res;
    }

    @DeleteMapping("/products/{id}")
    public Product deleteProductById(@PathVariable Integer id) throws JsonProcessingException, ProductNotFoundException {
        Product product = productService.deleteProductById(id);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"delete", product.getName()}));
        return product;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setPublisher(KafkaToInventoryMessagePublisher publisher) {
        this.publisher = publisher;
    }
}
