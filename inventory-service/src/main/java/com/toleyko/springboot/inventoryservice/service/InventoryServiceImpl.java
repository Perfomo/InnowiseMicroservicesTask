package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
        inventoryRepository.save(oldRemainder);
        return oldRemainder;
    }

    @Autowired
    public void setRemainderRepository(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
}
