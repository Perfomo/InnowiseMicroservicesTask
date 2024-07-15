package com.toleyko.springboot.inventoryservice.handlers;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.dto.OrderDto;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.InsufficientQuantityException;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Component
@Slf4j
public class OrderHandler {
    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;
    @Transactional
    public OrderDto handleOrder(OrderDto order) throws RemainderNotFoundException {
        boolean isOK = true;
        BigDecimal orderCost = BigDecimal.ZERO;
        for (Map.Entry<String, String> entry : order.getProducts().entrySet()) {
            String name = entry.getKey();
            String amount = entry.getValue();
            log.info("name: " + name + "\n" + "amount: " + amount);
            Remainder remainder = inventoryService.getRemainderByName(name);
            log.info("Remainder: " + remainder);
            if (remainder == null || remainder.getLeft() < Integer.parseInt(amount)) {
                order.getProducts().replace(name, amount + " not enough at storage for order");
                isOK = false;
            }
            if (isOK) {
                remainder.setLeft(remainder.getLeft() - Integer.parseInt(amount));
                remainder.setSold(remainder.getSold() + Integer.parseInt(amount));

                orderCost = orderCost.add(remainder.getCost().multiply(BigDecimal.valueOf(Integer.parseInt(amount))));
                inventoryRepository.save(remainder);
            }
        }
        if (isOK) {
            order.setCost(orderCost);
            order.setStatus(OrderStatus.OK);
        }
        else {
            order.setStatus(OrderStatus.ERROR);
            order.setCost(BigDecimal.ZERO);
        }
        return order;
    }
}
