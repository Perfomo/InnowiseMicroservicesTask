package com.toleyko.springboot.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toleyko.springboot.orderservice.dao.OrderHistoryRepository;
import com.toleyko.springboot.orderservice.entity.History;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.mapper.OrderToOrderHistoryMapper;
import com.toleyko.springboot.orderservice.service.impls.OrdersHistoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrdersHistoryServiceImplTest {

    private OrdersHistoryServiceImpl ordersHistoryService;
    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ordersHistoryService = new OrdersHistoryServiceImpl(orderHistoryRepository);
    }

    @Test
    public void save_SuccessfulTest() throws JsonProcessingException {
        Order order = new Order();
        History history = new History();
        history.setOrderHistory(OrderToOrderHistoryMapper.convert(order));
        when(orderHistoryRepository.save(any())).thenReturn(history);
        Assertions.assertEquals(history.getOrderHistory(), ordersHistoryService.save(order).getOrderHistory());
        Assertions.assertInstanceOf(History.class, ordersHistoryService.save(order));
    }
}
