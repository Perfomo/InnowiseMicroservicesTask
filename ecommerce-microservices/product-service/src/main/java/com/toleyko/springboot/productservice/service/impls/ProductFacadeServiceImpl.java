package com.toleyko.springboot.productservice.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.productservice.entity.Product;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import com.toleyko.springboot.productservice.service.ProductFacadeService;
import com.toleyko.springboot.productservice.service.ProductService;
import com.toleyko.springboot.productservice.service.kafka.KafkaToInventoryMessagePublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductFacadeServiceImpl implements ProductFacadeService {
    private ProductService productService;
    private KafkaToInventoryMessagePublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Product saveAndSendProduct(Product product) throws JsonProcessingException {
        Product res = productService.saveProduct(product);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"save", res.getName(), res.getCost().toString()}));
        return res;
    }
    @Override
    public Product updateAndSendProduct(Product product, Long id) throws ProductNotFoundException, JsonProcessingException {
        String oldName = productService.getProductById(id).getName();
        Product res = productService.updateProductById(id, product);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"update", oldName +"~"+ res.getName(), res.getCost().toString()}));
        return res;
    }
    @Override
    public Product deleteAndSendProduct(Long id) throws ProductNotFoundException, JsonProcessingException {
        Product product = productService.deleteProductById(id);
        publisher.sendMessageToTopic(objectMapper.writeValueAsString(new String[] {"delete", product.getName()}));
        return product;
    }
}
