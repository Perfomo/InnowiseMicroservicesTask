package com.toleyko.springboot.inventoryservice.handlers;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.dto.Order;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.InsufficientQuantityException;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class OrderHandler {
    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;
    @Transactional
    public Order handleOrder(Order order) {
        try {
            double orderCost = 0;
            for (Map.Entry<String, Integer> entry : order.getProducts().entrySet()) {
                String id = entry.getKey();
                Integer amount = entry.getValue();

                Remainder remainder = inventoryService.getRemainderById(Integer.valueOf(id));
                if (remainder == null || remainder.getLeft() < amount) {
                    throw new InsufficientQuantityException("Not enough: " + id + "\tOr product not found");
                }
                remainder.setLeft(remainder.getLeft() - amount);
                remainder.setSold(remainder.getSold() + amount);
                orderCost += remainder.getCost() * amount;
                inventoryRepository.save(remainder);
            }
            order.setCost(orderCost);
            order.setStatus("OK");
        } catch (InsufficientQuantityException | RemainderNotFoundException e) {
            order.setStatus("ERROR");
            order.setCost(0.0);
        }
        return order;
    }

    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Autowired
    public void setInventoryRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
}
