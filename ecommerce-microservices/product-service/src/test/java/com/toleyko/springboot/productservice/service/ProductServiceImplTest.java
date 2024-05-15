package com.toleyko.springboot.productservice.service;

import com.toleyko.springboot.productservice.dao.ProductRepository;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductServiceImpl productService = new ProductServiceImpl();

    @BeforeEach
    public void setUp() {
        productService.setProductRepository(productRepository);
    }

    @Test
    public void getProductBuId_SuccessfulTest() throws ProductNotFoundException {
        Integer id = 1;
        Product product = new Product().setId(id).setName("car").setCost(10.1);
        Optional<Product> optional = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optional);

        Assertions.assertEquals(product, productService.getProductById(id));
    }
    @Test
    public void getProductById_ProductNotFoundException() {
        Optional<Product> optional = Optional.empty();
        when(productRepository.findById(1)).thenReturn(optional);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1));
    }

    @Test
    public void getProductBuName_SuccessfulTest() throws ProductNotFoundException {
        String name = "pc";
        Product product = new Product().setId(1).setName(name).setCost(10.1);
        Optional<Product> optional = Optional.of(product);
        when(productRepository.findByName(name)).thenReturn(optional);

        Assertions.assertEquals(product, productService.getProductByName(name));
    }
    @Test
    public void getProductByName_ProductNotFoundException() {
        String name = "pc";
        Optional<Product> optional = Optional.empty();
        when(productRepository.findByName(name)).thenReturn(optional);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductByName(name));
    }

    @Test
    public void deleteProductById_SuccessfulTest() throws ProductNotFoundException {
        Integer id = 1;
        Product product = new Product().setId(id).setName("car").setCost(10.1);
        Optional<Product> optional = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optional);
        doNothing().when(productRepository).deleteById(id);

        Assertions.assertEquals(product, productService.deleteProductById(id));
        verify(productRepository, times(1)).deleteById(id);
    }
    @Test
    public void deleteProductById_ProductNotFoundException() {
        Optional<Product> optional = Optional.empty();
        when(productRepository.findById(1)).thenReturn(optional);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProductById(anyInt()));
        verify(productRepository, times(0)).deleteById(anyInt());
    }

    @Test
    public void updateProductById_SuccessfulTest() throws ProductNotFoundException {
        Integer id = 1;
        Product product = new Product().setId(id).setName("car").setCost(10.1);
        Optional<Product> optional = Optional.of(product);
        when(productRepository.findById(id)).thenReturn(optional);
        when(productRepository.save(product)).thenReturn(product);

        Assertions.assertEquals(product, productService.updateProductById(id, product));
        verify(productRepository, times(1)).save(product);
    }
    @Test
    public void updateProductById_ProductNotFoundException() {
        Integer id = 1;
        Product product = new Product().setId(id).setName("car").setCost(10.1);
        Optional<Product> optional = Optional.empty();
        when(productRepository.findById(1)).thenReturn(optional);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProductById(id, product));
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
