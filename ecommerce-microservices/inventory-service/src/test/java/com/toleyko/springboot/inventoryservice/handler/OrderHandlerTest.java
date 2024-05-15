package com.toleyko.springboot.inventoryservice.handler;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.dto.Order;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.OrderHandler;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class OrderHandlerTest {

    @Mock
    private InventoryService inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;
    private OrderHandler orderHandler;
    private Order order;
    private Remainder remainder;
    private Map<String, Integer> products;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderHandler = new OrderHandler();
        orderHandler.setInventoryRepository(inventoryRepository);
        orderHandler.setInventoryService(inventoryService);
        order = new Order()
                .setId(1)
                .setCost(0.0)
                .setUsername("Test")
                .setUserId("3")
                .setStatus("in");

        remainder = new Remainder()
                .setId(3)
                .setName("pc")
                .setCost(10.0)
                .setLeft(10)
                .setSold(0);

        products = new HashMap<>();
        products.put("1", 2);
        products.put("2", 3);
        order.setProducts(products);

    }

    @Test
    public void handleOrder_SuccessfulTest() throws RemainderNotFoundException {
        Order expected = new Order()
                .setId(1)
                .setCost(50.0)
                .setUsername("Test")
                .setUserId("3")
                .setStatus("OK");
        expected.setProducts(products);
        doReturn(remainder).when(inventoryService).getRemainderById(anyInt());
        when(inventoryRepository.save(any(Remainder.class))).thenReturn(remainder);

        Assertions.assertEquals(expected, orderHandler.handleOrder(order));
        verify(inventoryService, times(2)).getRemainderById(anyInt());
        verify(inventoryRepository, times(2)).save(any(Remainder.class));
    }

    @Test
    public void handleOrder_UnsuccessfulTest() throws RemainderNotFoundException {
        remainder.setLeft(2);
        Order expected = new Order()
                .setId(1)
                .setCost(0.0)
                .setUsername("Test")
                .setUserId("3")
                .setStatus("ERROR");
        expected.setProducts(products);
        doReturn(remainder).when(inventoryService).getRemainderById(anyInt());
        when(inventoryRepository.save(any(Remainder.class))).thenReturn(remainder);

        Assertions.assertEquals(expected, orderHandler.handleOrder(order));
        verify(inventoryService, times(2)).getRemainderById(anyInt());
        verify(inventoryRepository, times(1)).save(any(Remainder.class));
    }
}
