package com.toleyko.springboot.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.ProductFacadeService;
import com.toleyko.springboot.productservice.service.kafka.KafkaToInventoryMessagePublisher;
import com.toleyko.springboot.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor()
@RequestMapping("/api")
public class ProductController {
    private ProductFacadeService productFacadeService;
    private ProductService productService;

    @PreAuthorize("permitAll()")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/products/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/products")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) throws JsonProcessingException {
        return ResponseEntity.ok(productFacadeService.saveAndSendProduct(product));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) throws ProductNotFoundException, JsonProcessingException {
        return ResponseEntity.ok(productFacadeService.updateAndSendProduct(product, id));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProductById(@PathVariable Long id) throws JsonProcessingException, ProductNotFoundException {
        return ResponseEntity.ok(productFacadeService.deleteAndSendProduct(id));
    }
}
