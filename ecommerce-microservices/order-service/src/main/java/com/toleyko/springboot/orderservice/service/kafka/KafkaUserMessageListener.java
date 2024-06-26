package com.toleyko.springboot.orderservice.service.kafka;

import com.toleyko.springboot.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaUserMessageListener {
    private OrderService orderService;
    @KafkaListener(topics = "deleted-users-orders", groupId = "deleted-users-orders-group-1")
    public void consume(String message) {
        log.info(message);
        orderService.deleteOrderByUsername(message);
        log.info(message + "'s orders deleted");
    }
}
