package com.toleyko.springboot.inventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "inventory")
public class Remainder {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    @NotEmpty(message = "Invalid name")
    private String name;

    @Column(name = "amountLeft")
    @Min(value = 0, message = "Left amount must be 0 or more")
    private Integer left;

    @Column(name = "amountSold")
    @Min(value = 0, message = "Sold amount must be 0 or more")
    private Integer sold;

    @Column(name = "cost")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be more than 0")
    private Double cost;
}
