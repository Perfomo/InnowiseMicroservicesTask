package com.toleyko.springboot.orderservice.service;

import com.toleyko.springboot.orderservice.dao.OrderRepository;
import com.toleyko.springboot.orderservice.entity.Order;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void deleteOrderById_SuccessfulTest() throws OrderNotFoundException {
        Integer id = 1;
        Order order = new Order().setStatus("ok");
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(order, orderService.deleteOrderById(id));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).deleteById(id);
    }
    @Test
    public void deleteOrderById_OrderNotFoundExceptionTest() {
        Integer id = 1;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrderById(id));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(0)).deleteById(id);
    }

    @Test
    public void updateOrderById_SuccessfulTest() throws OrderNotFoundException {
        Integer id = 1;
        Order order = new Order().setStatus("ok");
        Optional<Order> optional = Optional.of(order);

        when(orderRepository.findById(1)).thenReturn(optional);
        when(orderRepository.save(order)).thenReturn(order);

        Assertions.assertEquals(order, orderService.updateOrderById(id, order));
        verify(orderRepository, times(1)).findById(id);
        verify(orderRepository, times(1)).save(order);
    }
    @Test
    public void updateOrderById_OrderNotFoundExceptionTest() {
        Integer id = 1;

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
    @Test
    public void getOrdersByUsername_OrderNotFoundExceptionTest() {
        List<Order> orderList = new ArrayList<>();

        when(orderRepository.getOrderByUsername(anyString())).thenReturn(orderList);

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByUsername(anyString()));
        verify(orderRepository, times(1)).getOrderByUsername(anyString());
    }


}
