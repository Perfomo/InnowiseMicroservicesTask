package com.toleyko.springboot.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class Order {
    private Integer id;
    private String status;
    private Map<String, Integer> names;
    private Double cost;
}
