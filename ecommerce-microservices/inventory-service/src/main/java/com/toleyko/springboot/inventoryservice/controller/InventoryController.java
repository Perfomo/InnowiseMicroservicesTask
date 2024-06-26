package com.toleyko.springboot.inventoryservice.controller;

import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import com.toleyko.springboot.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping("/inventory")
    public ResponseEntity<List<Remainder>> getAllRemainders() {
        return ResponseEntity.ok(inventoryService.getAllRemainders());
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Remainder> getRemainderById(@PathVariable Long id) throws RemainderNotFoundException {
        return ResponseEntity.ok(inventoryService.getRemainderById(id));
    }

    @GetMapping("/inventory/name/{name}")
    public ResponseEntity<Remainder> getRemainderByName(@PathVariable String name) throws RemainderNotFoundException {
        return ResponseEntity.ok(inventoryService.getRemainderByName(name));
    }

    @PostMapping("/inventory")
    public ResponseEntity<Remainder> saveRemainder(@Valid @RequestBody Remainder remainder) {
        return ResponseEntity.ok(inventoryService.saveRemainder(remainder));
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<Remainder> updateRemainder(@PathVariable Long id, @Valid @RequestBody Remainder remainder) throws RemainderNotFoundException {
        return ResponseEntity.ok(inventoryService.updateRemainderById(id, remainder));
    }

    @PutMapping("/inventory/{id}/amount")
    public ResponseEntity<Remainder> updateRemainderLeftAmountById(@PathVariable Long id, @RequestParam Integer amount) throws RemainderNotFoundException {
        return ResponseEntity.ok(inventoryService.updateRemainderLeftAmount(id, amount));
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<Remainder> deleteRemainderById(@PathVariable Long id) throws RemainderNotFoundException {
        return ResponseEntity.ok(inventoryService.deleteRemainderById(id));
    }
}
