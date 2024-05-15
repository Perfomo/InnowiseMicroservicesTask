package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;

import java.util.List;
public interface InventoryService {
    public List<Remainder> getAllRemainders();
    public Remainder getRemainderById(Integer id) throws RemainderNotFoundException;
    public Remainder getRemainderByName(String name) throws RemainderNotFoundException;
    public Remainder deleteRemainderById(Integer id) throws RemainderNotFoundException;
    public Remainder saveRemainder(Remainder remainder);
    public Remainder updateRemainderById(Integer id, Remainder remainder) throws RemainderNotFoundException;
    public Remainder updateRemainderLeftAmount(Integer id, Integer amount) throws RemainderNotFoundException;
}
