package com.toleyko.springboot.orderservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toleyko.springboot.orderservice.dto.OrderHistory;
import com.toleyko.springboot.orderservice.entity.Order;

public class OrderToOrderHistoryMapper {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static OrderHistory convert(Order order) throws JsonProcessingException {
        return new OrderHistory()
                .setCost(order.getCost())
                .setProducts(objectMapper.writeValueAsString(order.getProducts()))
                .setStatus(order.getStatus())
                .setUsername(order.getUsername())
                .setUserId(order.getUserId())
                .setOrderId(order.getId());
    }
}
