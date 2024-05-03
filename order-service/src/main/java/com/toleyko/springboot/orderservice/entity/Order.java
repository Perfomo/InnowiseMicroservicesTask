package com.toleyko.springboot.orderservice.entity;

import jakarta.persistence.*;
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

    @ElementCollection
    @CollectionTable(name = "product_list", joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "product_name")
    @Column(name = "producNames")
    private Map<String, Integer> names;

    @Column(name = "cost")
    private Double cost;
}
