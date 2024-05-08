package com.toleyko.springboot.inventoryservice.controller;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InventoryController {
    private InventoryService inventoryService;
    @GetMapping("/inventory")
    public List<Remainder> getAllRemainders() {
        return inventoryService.getAllRemainders();
    }

    @GetMapping("/inventory/{id}")
    public Remainder getRemainderById(@PathVariable Integer id) throws RemainderNotFoundException {
        return inventoryService.getRemainderById(id);
    }

    @PostMapping("/inventory")
    public Remainder saveRemainder(@RequestBody Remainder remainder) {
        return inventoryService.saveRemainder(remainder);
    }

    @PutMapping("/inventory/{id}")
    public Remainder updateRemainder(@PathVariable Integer id, @RequestBody Remainder remainder) throws RemainderNotFoundException {
        return inventoryService.updateRemainderById(id, remainder);
    }

    @PutMapping("/inventory/{id}/amount")
    public Remainder updateRemainderLeftAmountById(@PathVariable Integer id, @RequestParam Integer amount) throws RemainderNotFoundException {
        return inventoryService.updateRemainderLeftAmount(id, amount);
    }

    @DeleteMapping("/inventory/{id}")
    public Remainder deleteRemainderById(@PathVariable Integer id) throws RemainderNotFoundException {
        return inventoryService.deleteRemainderById(id);
    }

    @Autowired
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
}
