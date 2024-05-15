package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.dao.ProductRepository;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Integer id) throws ProductNotFoundException {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public Product getProductByName(String name) throws ProductNotFoundException {
        Optional<Product> optional = productRepository.findByName(name);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public Product deleteProductById(Integer id) throws ProductNotFoundException {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            productRepository.deleteById(id);
            return optional.get();
        }
        throw new ProductNotFoundException("Product not found");
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProductById(Integer id, Product product) throws ProductNotFoundException {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            Product oldProduct = optional.get();
            oldProduct.setName(product.getName());
            oldProduct.setCost(product.getCost());
            return productRepository.save(oldProduct);
        }
        throw new ProductNotFoundException("Product not found");
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
