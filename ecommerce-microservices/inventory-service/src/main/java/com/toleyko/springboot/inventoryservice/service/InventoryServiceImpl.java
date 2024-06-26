package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {
    private InventoryRepository inventoryRepository;
    @Override
    public List<Remainder> getAllRemainders() {
        return inventoryRepository.findAll();
    }

    @Override
    public Remainder getRemainderById(Long id) throws RemainderNotFoundException {
        return inventoryRepository.findById(id).orElseThrow(() -> new RemainderNotFoundException("Product not found"));
    }

    @Override
    public Remainder getRemainderByName(String name) throws RemainderNotFoundException {
        return inventoryRepository.findByName(name).orElseThrow(() -> new RemainderNotFoundException("Product not found"));
    }

    @Override
    public Remainder deleteRemainderById(Long id) throws RemainderNotFoundException {
        Remainder remainder = inventoryRepository.findById(id).orElseThrow(() -> new RemainderNotFoundException("Product not found"));
        inventoryRepository.deleteById(id);
        return remainder;
    }

    @Override
    public Remainder saveRemainder(Remainder remainder) {
        return inventoryRepository.save(remainder);
    }

    @Override
    public Remainder updateRemainderById(Long id, Remainder remainder) throws RemainderNotFoundException {
        Remainder oldRemainder = inventoryRepository.findById(id).orElseThrow(() -> new RemainderNotFoundException("Product not found"));
        oldRemainder.setName(remainder.getName());
        oldRemainder.setLeft(remainder.getLeft());
        oldRemainder.setSold(remainder.getSold());
        oldRemainder.setCost(remainder.getCost());
        return inventoryRepository.save(oldRemainder);
    }

    @Override
    public Remainder updateRemainderLeftAmount(Long id, Integer amount) throws RemainderNotFoundException {
        Remainder oldRemainder = inventoryRepository.findById(id).orElseThrow(() -> new RemainderNotFoundException("Product not found"));
        oldRemainder.setLeft(oldRemainder.getLeft() + amount);
        return inventoryRepository.save(oldRemainder);
    }
}