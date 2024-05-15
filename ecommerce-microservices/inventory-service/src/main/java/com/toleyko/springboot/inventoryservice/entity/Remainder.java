package com.toleyko.springboot.inventoryservice.entity;

import jakarta.persistence.*;
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
    private String name;

    @Column(name = "amountLeft")
    private Integer left;

    @Column(name = "amountSold")
    private Integer sold;

    @Column(name = "cost")
    private Double cost;
}
