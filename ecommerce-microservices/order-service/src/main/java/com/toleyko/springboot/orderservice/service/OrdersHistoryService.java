package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;

import java.util.List;

public interface OrdersHistoryService {
    public List<History> getHistory();
    public History save(Order order) throws JsonProcessingException;
}
