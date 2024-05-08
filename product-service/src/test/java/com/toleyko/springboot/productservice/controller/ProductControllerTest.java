package com.toleyko.springboot.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.GlobalProductHandler;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.KafkaToInventoryMessagePublisher;
import com.toleyko.springboot.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
public class ProductControllerTest {
    @Mock
    private ProductService productService;
    @Mock
    private KafkaToInventoryMessagePublisher publisher;
    private ProductController productController = new ProductController();
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productController.setProductService(productService);
        productController.setPublisher(publisher);
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalProductHandler())
                .build();
    }

    @Test
    public void getAllProducts_SuccessfulTest() throws Exception {
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add((new Product()).setName("Car").setCost(10.1));
        expectedProducts.add((new Product()).setName("PC").setCost(10.1));
        when(productService.getAllProducts()).thenReturn(expectedProducts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Car"))
                .andExpect(jsonPath("$[0].cost").value(10.1))
                .andExpect(jsonPath("$[1].name").value("PC"))
                .andExpect(jsonPath("$[1].cost").value(10.1));
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void getProductById_SuccessfulTest() throws Exception {
        Integer id = 1;
        Product product = new Product().setId(id).setName("Car").setCost(10.1);
        when(productService.getProductById(id)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.cost").exists())
                .andExpect(jsonPath("$.id").value(id));
        verify(productService, times(1)).getProductById(id);
    }
    @Test
    public void getProductById_ProductNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(productService.getProductById(id)).thenThrow(ProductNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(productService, times(1)).getProductById(id);
    }

    @Test
    public void saveProduct_SuccessfulTest() throws Exception {
        Product product = new Product().setId(1).setName("Kir").setCost(10.1);
        when(productService.saveProduct(product)).thenReturn(product);
        doNothing().when(publisher).sendMessageToTopic(anyString());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(product)))
                .andExpect(status().isOk())
                .andReturn();
        verify(productService, times(1)).saveProduct(product);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseBody);
    }
    @Test
    public void saveProduct_JsonProcessingExceptionTest() throws Exception {
        Product product = new Product().setId(1).setName("Kir").setCost(10.1);
        when(productService.saveProduct(product)).thenReturn(product);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productController, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        doNothing().when(publisher).sendMessageToTopic(anyString());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProduct_SuccessfulTest() throws Exception {
        Integer id = 1;
        Product product = new Product().setId(id).setName("Car").setCost(10.1);
        when(productService.updateProductById(id, product)).thenReturn(product);
        when(productService.getProductById(id)).thenReturn(product);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(product)))
                .andExpect(status().isOk())
                .andReturn();
        verify(productService, times(1)).updateProductById(id, product);
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertNotNull(responseBody);
    }
    @Test
    public void updateProduct_ProductNotFoundExceptionTest() throws Exception {
        Integer id = 1;
        when(productService.getProductById(id)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Product())))
                .andExpect(status().isNotFound());
        verify(productService, times(1)).getProductById(id);
    }
    @Test
    public void updateProduct_JsonProcessingExceptionTest() throws Exception {
        Integer id = 1;
        Product product = new Product().setName("pr");
        Product product1 = new Product().setName("ds");
        when(productService.getProductById(id)).thenReturn(product);
        when(productService.updateProductById(id, product1)).thenReturn(product1);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productController, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        doNothing().when(publisher).sendMessageToTopic(anyString());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(product1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteProductById_SuccessfulTest() throws Exception {
        Integer id = 1;

        doNothing().when(productService).deleteProductById(id);
        when(productService.getProductById(id)).thenReturn(new Product());
        doNothing().when(publisher).sendMessageToTopic(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", id))
                .andExpect(status().isOk());
        verify(productService, times(1)).deleteProductById(id);
    }
    @Test
    public void deleteProductById_ProductNotFoundExceptionTest() throws Exception {
        Integer id = 1;

        when(productService.getProductById(id)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", id))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(id);
        verify(productService, times(0)).deleteProductById(id);
    }
    @Test
    public void deleteProductById_JsonProcessingExceptionTest() throws Exception {
        Integer id = 1;
        Product product = new Product().setName("pc");
        when(productService.getProductById(id)).thenReturn(product);
        doNothing().when(productService).deleteProductById(id);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ReflectionTestUtils.setField(productController, "objectMapper", objectMapper);
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", id))
                .andExpect(status().isBadRequest());

        verify(productService, times(1)).getProductById(id);
        verify(productService, times(1)).deleteProductById(id);
    }

    private static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
