package com.toleyko.springboot.inventoryservice.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Order {
    private Integer id;
    private String status;
    private String userId;
    private Map<String, Integer> names;
    private Double cost;
}
