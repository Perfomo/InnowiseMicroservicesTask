package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;

import java.util.List;
public interface InventoryService {
    public List<Remainder> getAllRemainders();
    public Remainder getRemainderById(Long id) throws RemainderNotFoundException;
    public Remainder getRemainderByName(String name) throws RemainderNotFoundException;
    public Remainder deleteRemainderById(Long id) throws RemainderNotFoundException;
    public Remainder saveRemainder(Remainder remainder);
    public Remainder updateRemainderById(Long id, Remainder remainder) throws RemainderNotFoundException;
    public Remainder updateRemainderLeftAmount(Long id, Integer amount) throws RemainderNotFoundException;
}
