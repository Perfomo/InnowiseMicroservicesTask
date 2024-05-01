package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InventoryServiceTest {
    private final InventoryRepository inventoryRepository = mock(InventoryRepository.class);
    private final InventoryServiceImpl inventoryService = new InventoryServiceImpl();

    @Test
    public void getRemainderById_SuccessfulTest() throws RemainderNotFoundException {
        inventoryService.setRemainderRepository(inventoryRepository);
        Integer id = 1;
        Remainder remainder = new Remainder().setId(1).setName("PC").setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findById(id)).thenReturn(optional);

        Assertions.assertEquals(remainder, inventoryService.getRemainderById(id));
    }

    @Test
    public void getRemainderById_RemainderNotFoundExceptionTest() {
        inventoryService.setRemainderRepository(inventoryRepository);
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findById(1)).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.getRemainderById(1));
    }
}
