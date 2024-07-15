package com.toleyko.springboot.orderservice.entity;

import com.toleyko.springboot.orderservice.handler.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @NotEmpty(message = "Products list is empty")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_list", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "products")
    private Map<String, String> products;

    @Column(name = "cost")
    private BigDecimal cost;
}
