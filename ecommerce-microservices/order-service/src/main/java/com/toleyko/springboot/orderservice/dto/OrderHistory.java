package com.toleyko.springboot.orderservice.dto;

import com.toleyko.springboot.orderservice.handler.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Embeddable
@Data
@Accessors(chain = true)
public class OrderHistory {
    private Long orderId;
    private OrderStatus status;
    private String username;
    private String userId;
    @ElementCollection
    @CollectionTable(name = "history_prder_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> products;
    private BigDecimal cost;
}
