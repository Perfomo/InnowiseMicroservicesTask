package com.toleyko.springboot.productservice.service.impls;

import com.toleyko.springboot.productservice.dao.ProductRepository;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public Product getProductByName(String name) throws ProductNotFoundException {
        return productRepository.findByName(name).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public Product deleteProductById(Long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        productRepository.deleteById(id);
        return product;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProductById(Long id, Product product) throws ProductNotFoundException {
        Product oldProduct = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        oldProduct.setName(product.getName());
        oldProduct.setCost(product.getCost());
        return productRepository.save(oldProduct);
    }
}
