package com.toleyko.springboot.inventoryservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Order {
    private Integer id;
    private String status;
    private String username;
    private Map<String, Integer> names;
    private Double cost;
}
