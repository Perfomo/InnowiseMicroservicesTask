package com.toleyko.springboot.productservice.controller;

import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;


    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/products")
    public List<Product> getAllStudents() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getStudentById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping("/products")
    public Product saveStudent(@RequestBody Product product) {
        productService.saveProduct(product);
        return product;
    }

    @PutMapping("/products")
    public Product updateStudent(@RequestBody Product product) {
        productService.saveProduct(product);
        return product;
    }

    @DeleteMapping("/products/{id}")
    public void deleteStudentById(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }

    @Autowired
    public void setStudentService(ProductService productService) {
        this.productService = productService;
    }
}
