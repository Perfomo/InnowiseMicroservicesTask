package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.dao.ProductRepository;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductServiceImpl productService = new ProductServiceImpl();

    @Test
    public void getProductBuId_SuccessfulTest() throws ProductNotFoundException {
        productService.setProductRepository(productRepository);
        Integer id = 1;
        Product product = new Product().setId(id).setName("car").setCost(10);
        Optional<Product> optional = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optional);

        Assertions.assertEquals(product, productService.getProductById(id));
    }

    @Test
    public void getProductById_ProductNotFoundException() {
        productService.setProductRepository(productRepository);
        Optional<Product> optional = Optional.empty();
        when(productRepository.findById(1)).thenReturn(optional);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1));
    }
}
