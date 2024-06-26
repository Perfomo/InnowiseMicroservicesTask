package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;

public interface OrderFacadeService {
    public Order saveAndSendOrder(Order order) throws TokenDataExtractionException, JsonProcessingException;
}
