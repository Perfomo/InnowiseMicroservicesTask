package com.toleyko.springboot.orderservice.service.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.dao.OrderHistoryRepository;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.mapper.OrderToOrderHistoryMapper;
import com.toleyko.springboot.orderservice.service.OrdersHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
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
                .setTime(Instant.now());
        return orderHistoryRepository.save(history);
    }
}
