package com.toleyko.springboot.inventoryservice.handlers;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.dto.OrderDto;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.InsufficientQuantityException;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Component
public class OrderHandler {
    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;
    @Transactional
    public OrderDto handleOrder(OrderDto order) {
        try {
            BigDecimal orderCost = BigDecimal.ZERO;
            for (Map.Entry<String, Integer> entry : order.getProducts().entrySet()) {
                String id = entry.getKey();
                Integer amount = entry.getValue();

                Remainder remainder = inventoryService.getRemainderById(Long.valueOf(id));
                if (remainder == null || remainder.getLeft() < amount) {
                    throw new InsufficientQuantityException("Not enough: " + id + "\tOr product not found");
                }
                remainder.setLeft(remainder.getLeft() - amount);
                remainder.setSold(remainder.getSold() + amount);

                orderCost = orderCost.add(remainder.getCost().multiply(BigDecimal.valueOf(amount)));
                inventoryRepository.save(remainder);
            }
            order.setCost(orderCost);
            order.setStatus(OrderStatus.OK);
        } catch (InsufficientQuantityException | RemainderNotFoundException e) {
            order.setStatus(OrderStatus.ERROR);
            order.setCost(BigDecimal.ZERO);
        }
        return order;
    }
}
