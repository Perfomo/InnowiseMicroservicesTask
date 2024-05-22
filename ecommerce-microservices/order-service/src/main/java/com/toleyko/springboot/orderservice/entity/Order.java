package com.toleyko.springboot.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status")
    private String status;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @NotEmpty(message = "Products list is empty")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_list", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "products")
    private Map<String, Integer> products;

    @Column(name = "cost")
    private Double cost;
}
