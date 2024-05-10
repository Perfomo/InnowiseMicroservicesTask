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
    public Remainder getRemainderByName(String name) throws RemainderNotFoundException {
        Optional<Remainder> optional = inventoryRepository.findByName(name);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new RemainderNotFoundException("Product not found");
    }

    @Override
    public Remainder deleteRemainderById(Integer id) throws RemainderNotFoundException {
        Optional<Remainder> optional = inventoryRepository.findById(id);
        if (optional.isPresent()) {
            inventoryRepository.deleteById(id);
            return optional.get();
        }
        throw new RemainderNotFoundException("Product not found");
    }

    @Override
    public Remainder saveRemainder(Remainder remainder) {
        return inventoryRepository.save(remainder);
    }

    @Override
    public Remainder updateRemainderById(Integer id, Remainder remainder) throws RemainderNotFoundException {
        Optional<Remainder> optional = inventoryRepository.findById(id);
        if (optional.isPresent()) {
            Remainder oldRemainder = optional.get();
            oldRemainder.setName(remainder.getName());
            oldRemainder.setLeft(remainder.getLeft());
            oldRemainder.setSold(remainder.getSold());
            oldRemainder.setCost(remainder.getCost());
            return inventoryRepository.save(oldRemainder);
        }
        throw new RemainderNotFoundException("Product not found");
    }

    @Override
    public Remainder updateRemainderLeftAmount(Integer id, Integer amount) throws RemainderNotFoundException {
        Optional<Remainder> optional = inventoryRepository.findById(id);
        if (optional.isPresent()) {
            Remainder oldRemainder = optional.get();
            oldRemainder.setLeft(oldRemainder.getLeft() + amount);
            return inventoryRepository.save(oldRemainder);
        }
        throw new RemainderNotFoundException("Product not found");
    }

    @Transactional
    @Override
    public Order handleOrder(Order order) {
        try {
            double orderCost = 0;
            for (Map.Entry<String, Integer> entry : order.getProducts().entrySet()) {
                String id = entry.getKey();
                Integer amount = entry.getValue();

                Remainder remainder = this.getRemainderById(Integer.valueOf(id));
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
    public void setRemainderRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
}