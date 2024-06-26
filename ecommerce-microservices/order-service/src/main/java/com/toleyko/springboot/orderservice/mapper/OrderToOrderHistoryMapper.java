package com.toleyko.springboot.orderservice.mapper;

import com.toleyko.springboot.orderservice.dto.OrderHistory;
import com.toleyko.springboot.orderservice.entity.Order;

public class OrderToOrderHistoryMapper {
    public static OrderHistory convert(Order order) {
        return new OrderHistory()
                .setCost(order.getCost())
                .setProducts(order.getProducts())
                .setStatus(order.getStatus())
                .setUsername(order.getUsername())
                .setUserId(order.getUserId())
                .setOrderId(order.getId());
    }
}
