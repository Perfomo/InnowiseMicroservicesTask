package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.OrderStatus;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.service.impls.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    public void getOrderById_SuccessfulTest() throws OrderNotFoundException {
        Long id = 1L;
        Order order = new Order().setStatus(OrderStatus.OK);
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1L)).thenReturn(optional);

        Assertions.assertEquals(order, orderService.getOrderById(id));
        verify(orderRepository, times(1)).findById(id);

    }
    @Test
    public void getOrderById_OrderNotFoundExceptionTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(id));
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    public void deleteOrderById_SuccessfulTest() throws OrderNotFoundException {
        Long id = 1L;
        Order order = new Order().setStatus(OrderStatus.OK);
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1L)).thenReturn(optional);

        Assertions.assertEquals(order, orderService.deleteOrderById(id));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).deleteById(id);
    }
    @Test
    public void deleteOrderById_OrderNotFoundExceptionTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrderById(id));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(0)).deleteById(id);
    }

    @Test
    public void updateOrderById_SuccessfulTest() throws OrderNotFoundException {
        Long id = 1L;
        Order order = new Order().setStatus(OrderStatus.OK);
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1L)).thenReturn(optional);
        when(orderRepository.save(order)).thenReturn(order);

        Assertions.assertEquals(order, orderService.updateOrderById(id, order));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(order);
    }
    @Test
    public void updateOrderById_OrderNotFoundExceptionTest() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.updateOrderById(id, new Order()));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(0)).save(new Order());
    }

    @Test
    public void getOrdersByUsername_SuccessfulTest() throws OrderNotFoundException {
        List<Order> orderList = new ArrayList<>();
        Order order = new Order();
        orderList.add(order);

        when(orderRepository.getOrderByUsername(anyString())).thenReturn(orderList);

        Assertions.assertEquals(orderList, orderService.getOrdersByUsername(anyString()));
        verify(orderRepository, times(1)).getOrderByUsername(anyString());
    }
}
