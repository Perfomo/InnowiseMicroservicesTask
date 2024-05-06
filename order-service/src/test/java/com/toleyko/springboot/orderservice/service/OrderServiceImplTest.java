package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @BeforeEach
    public void setUp() {
        orderService.setOrderRepository(orderRepository);
    }

    @Test
    public void getOrderById_SuccessfulTest() throws OrderNotFoundException {
        Integer id = 1;
        Order order = new Order().setStatus("ok");
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(order, orderService.getOrderById(id));
        verify(orderRepository, times(1)).findById(id);
    }
    @Test
    public void getOrderById_OrderNotFoundExceptionTest() {
        Integer id = 1;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(id));
        verify(orderRepository, times(1)).findById(id);
    }
}
