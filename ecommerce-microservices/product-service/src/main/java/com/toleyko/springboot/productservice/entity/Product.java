package com.toleyko.springboot.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Invalid name")
    @Column(name = "name", unique = true)
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be more than 0")
    @DecimalMax(value = "10000.0", inclusive = false, message = "Cost must be less than 10 000")
    @Column(name = "cost")
    private BigDecimal cost;
}
