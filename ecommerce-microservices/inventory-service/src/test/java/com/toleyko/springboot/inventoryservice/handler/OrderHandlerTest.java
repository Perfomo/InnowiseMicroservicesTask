//package com.toleyko.springboot.inventoryservice.handler;
//
//import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
//import com.toleyko.springboot.inventoryservice.dto.OrderDto;
//import com.toleyko.springboot.inventoryservice.entity.Remainder;
//import com.toleyko.springboot.inventoryservice.handlers.OrderHandler;
//import com.toleyko.springboot.inventoryservice.handlers.OrderStatus;
//import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
//import com.toleyko.springboot.inventoryservice.service.InventoryService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//
//public class OrderHandlerTest {
//    @Mock
//    private InventoryService inventoryService;
//    @Mock
//    private InventoryRepository inventoryRepository;
//    private OrderHandler orderHandler;
//    private OrderDto order;
//    private Remainder remainder;
//    private Map<String, String> products;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        orderHandler = new OrderHandler(inventoryService, inventoryRepository);
//        order = new OrderDto()
//                .setId(1L)
//                .setCost(BigDecimal.ZERO)
//                .setUsername("Test")
//                .setUserId("3")
//                .setStatus(OrderStatus.PENDING);
//
//        remainder = new Remainder()
//                .setId(3L)
//                .setName("pc")
//                .setCost(BigDecimal.valueOf(10.0))
//                .setLeft(10)
//                .setSold(0);
//
//        products = new HashMap<>();
//        products.put("pc", "2");
//        products.put("car", "3");
//        order.setProducts(products);
//
//    }
//
//    @Test
//    public void handleOrder_SuccessfulTest() throws RemainderNotFoundException {
//        OrderDto expected = new OrderDto()
//                .setId(1L)
//                .setCost(BigDecimal.valueOf(50.0))
//                .setUsername("Test")
//                .setUserId("3")
//                .setStatus(OrderStatus.OK);
//        expected.setProducts(products);
//        doReturn(remainder).when(inventoryService).getRemainderByName(anyString());
//        when(inventoryRepository.save(any(Remainder.class))).thenReturn(remainder);
//
//        Assertions.assertEquals(expected, orderHandler.handleOrder(order));
//        verify(inventoryService, times(2)).getRemainderByName(anyString());
//        verify(inventoryRepository, times(2)).save(any(Remainder.class));
//    }
//
//    @Test
//    public void handleOrder_UnsuccessfulTest() throws RemainderNotFoundException {
//        remainder.setLeft(2);
//        OrderDto expected = new OrderDto()
//                .setId(1L)
//                .setCost(BigDecimal.ZERO)
//                .setUsername("Test")
//                .setUserId("3")
//                .setStatus(OrderStatus.ERROR);
//        expected.setProducts(products);
//        doReturn(remainder).when(inventoryService).getRemainderByName(anyString());
//        when(inventoryRepository.save(any(Remainder.class))).thenReturn(remainder);
//
//        Assertions.assertEquals(expected, orderHandler.handleOrder(order));
//        verify(inventoryService, times(2)).getRemainderByName(anyString());
//        verify(inventoryRepository, times(1)).save(any(Remainder.class));
//    }
//}
