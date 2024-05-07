package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.dto.Order;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.InsufficientQuantityException;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;
    @Override
    public List<Remainder> getAllRemainders() {
        return inventoryRepository.findAll();
    }

    @Override
    public Remainder getRemainderById(Integer id) throws RemainderNotFoundException {
        Optional<Remainder> optional = inventoryRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new RemainderNotFoundException("Product not found");
    }

    @Override
    public Remainder getRemainderByName(String name) {
        return inventoryRepository.findByName(name);
    }

    @Override
    public void deleteRemainderById(Integer id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public Remainder saveRemainder(Remainder remainder) {
        return inventoryRepository.save(remainder);
    }

    @Override
    public Remainder updateRemainderById(Integer id, Remainder remainder) throws RemainderNotFoundException {
        Remainder oldRemainder = this.getRemainderById(id);
        oldRemainder.setName(remainder.getName());
        oldRemainder.setLeft(remainder.getLeft());
        oldRemainder.setSold(remainder.getSold());
        oldRemainder.setCost((remainder.getCost()));
        inventoryRepository.save(oldRemainder);
        return oldRemainder;
    }

    @Transactional
    @Override
    public Order handleOrder(Order order) {
        try {
            double orderCost = 0;
            for (Map.Entry<String, Integer> entry : order.getNames().entrySet()) {
                String name = entry.getKey();
                Integer amount = entry.getValue();

                Remainder remainder = this.getRemainderByName(name);
                if (remainder == null || remainder.getLeft() < amount) {
                    throw new InsufficientQuantityException("Not enough: " + name + "\tOr product not found");
                }
                remainder.setLeft(remainder.getLeft() - amount);
                remainder.setSold(remainder.getSold() + amount);
                orderCost += remainder.getCost() * amount;
                inventoryRepository.save(remainder);
            }
            order.setCost(orderCost);
            order.setStatus("OK");
        } catch (InsufficientQuantityException e) {
            order.setStatus("ERROR");
            order.setCost(0.0);
        }
        return order;
    }

    @Autowired
    public void setRemainderRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
}
