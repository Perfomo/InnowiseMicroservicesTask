package com.toleyko.springboot.inventoryservice.dto;

import com.toleyko.springboot.inventoryservice.handlers.OrderStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Accessors(chain = true)
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private String username;
    private String userId;
    private Map<String, String> products;
    private BigDecimal cost;
}
