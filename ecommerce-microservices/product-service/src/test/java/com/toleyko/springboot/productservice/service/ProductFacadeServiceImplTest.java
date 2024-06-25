package com.toleyko.springboot.productservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.impls.ProductFacadeServiceImpl;
import com.toleyko.springboot.productservice.service.kafka.KafkaToInventoryMessagePublisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;

public class ProductFacadeServiceImplTest {
    @Mock
    private ProductService productService;
    @Mock
    private KafkaToInventoryMessagePublisher publisher;
    private ProductFacadeService productFacadeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productFacadeService = new ProductFacadeServiceImpl(productService, publisher);
    }

    @Test
    public void saveAndSendProduct_SuccessfulTest() throws JsonProcessingException {
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.saveProduct(product)).thenReturn(product);
        doNothing().when(publisher).sendMessageToTopic(anyString());
        Assertions.assertEquals(product, productFacadeService.saveAndSendProduct(product));
    }
    @Test
    public void saveAndSendProduct_JsonProcessingExceptionTest() throws JsonProcessingException {
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.saveProduct(product)).thenReturn(product);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productFacadeService, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        Assertions.assertThrows(JsonProcessingException.class, () -> productFacadeService.saveAndSendProduct(product));
    }

    @Test
    public void updateAndSendProduct_SuccessfulTest() throws ProductNotFoundException, JsonProcessingException {
        Long id = 1L;
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.getProductById(id)).thenReturn(product);
        when(productService.updateProductById(id, product)).thenReturn(product);
        doNothing().when(publisher).sendMessageToTopic(anyString());
        Assertions.assertEquals(product, productFacadeService.updateAndSendProduct(product, id));
    }
    @Test
    public void updateAndSendProduct_ProductNotFoundExceptionTest() throws ProductNotFoundException {
        Long id = 1L;
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.getProductById(id)).thenThrow(ProductNotFoundException.class);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productFacadeService.updateAndSendProduct(product, id));
    }
    @Test
    public void updateAndSendProduct_JsonProcessingExceptionTest() throws ProductNotFoundException, JsonProcessingException {
        Long id = 1L;
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.getProductById(id)).thenReturn(product);
        when(productService.updateProductById(id, product)).thenReturn(product);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productFacadeService, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        Assertions.assertThrows(JsonProcessingException.class, () -> productFacadeService.updateAndSendProduct(product, id));
    }

    @Test
    public void deleteAndSendProduct_SuccessfulTest() throws JsonProcessingException, ProductNotFoundException {
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.deleteProductById(anyLong())).thenReturn(product);
        doNothing().when(publisher).sendMessageToTopic(anyString());
        Assertions.assertEquals(product, productFacadeService.deleteAndSendProduct(anyLong()));
    }
    @Test
    public void deleteAndSendProduct_ProductNotFoundExceptionTest() throws ProductNotFoundException {
        when(productService.deleteProductById(anyLong())).thenThrow(ProductNotFoundException.class);
        Assertions.assertThrows(ProductNotFoundException.class, () -> productFacadeService.deleteAndSendProduct(anyLong()));
    }
    @Test
    public void deleteAndSendProduct_JsonProcessingExceptionTest() throws JsonProcessingException, ProductNotFoundException {
        Product product = new Product().setName("pc").setId(1L).setCost(BigDecimal.ZERO);
        when(productService.deleteProductById(anyLong())).thenReturn(product);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productFacadeService, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        Assertions.assertThrows(JsonProcessingException.class, () -> productFacadeService.deleteAndSendProduct(anyLong()));
    }
}
