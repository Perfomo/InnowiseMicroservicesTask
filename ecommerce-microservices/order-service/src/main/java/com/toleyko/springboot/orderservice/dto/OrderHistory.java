package com.toleyko.springboot.orderservice.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.experimental.Accessors;

@Embeddable
@Data
@Accessors(chain = true)
public class OrderHistory {
    private Integer orderId;
    private String status;
    private String username;
    private String userId;
    private String products;
    private Double cost;
}
