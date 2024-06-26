package com.toleyko.springboot.orderservice.entity;

import com.toleyko.springboot.orderservice.dto.OrderHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_stamp")
    private Instant time;

    @Embedded
    @Column(name = "`order`")
    private OrderHistory OrderHistory;
}
