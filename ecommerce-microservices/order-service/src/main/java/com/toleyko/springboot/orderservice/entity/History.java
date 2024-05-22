package com.toleyko.springboot.orderservice.entity;

import com.toleyko.springboot.orderservice.dto.OrderHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "time_stamp")
    private LocalDateTime time;

    @Embedded
    @Column(name = "`order`")
    private OrderHistory OrderHistory;
}
