package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.dao.OrderHistoryRepository;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.mapper.OrderToOrderHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersHistoryServiceImpl implements OrdersHistoryService {
    private OrderHistoryRepository orderHistoryRepository;
    @Override
    public List<History> getHistory() {
        return orderHistoryRepository.findAll();
    }

    @Override
    public History save(Order order) throws JsonProcessingException {
        History history = new History()
                .setOrderHistory(OrderToOrderHistoryMapper.convert(order))
                .setTime(LocalDateTime.now());
        return orderHistoryRepository.save(history);
    }

    @Autowired
    public void setOrderHistoryRepository(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }
}
