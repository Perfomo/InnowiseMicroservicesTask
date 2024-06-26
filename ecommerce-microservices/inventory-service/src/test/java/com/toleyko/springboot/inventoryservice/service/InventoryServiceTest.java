package com.toleyko.springboot.inventoryservice.service;

import com.toleyko.springboot.inventoryservice.dao.InventoryRepository;
import com.toleyko.springboot.inventoryservice.entity.Remainder;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {
    private final InventoryRepository inventoryRepository = mock(InventoryRepository.class);
    private final InventoryServiceImpl inventoryService = new InventoryServiceImpl(inventoryRepository);

    @Test
    public void getRemainderById_SuccessfulTest() throws RemainderNotFoundException {
        Long id = 1L;
        Remainder remainder = new Remainder().setId(1L).setName("PC").setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findById(id)).thenReturn(optional);

        Assertions.assertEquals(remainder, inventoryService.getRemainderById(id));
        verify(inventoryRepository, times(1)).findById(id);
    }
    @Test
    public void getRemainderById_RemainderNotFoundExceptionTest() {
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findById(1L)).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.getRemainderById(1L));
        verify(inventoryRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getRemainderByName_SuccessfulTest() throws RemainderNotFoundException {
        String name = "name";
        Remainder remainder = new Remainder().setId(1L).setName(name).setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findByName(name)).thenReturn(optional);

        Assertions.assertEquals(remainder, inventoryService.getRemainderByName(name));
        verify(inventoryRepository, times(1)).findByName(name);
    }
    @Test
    public void getRemainderByName_RemainderNotFoundExceptionTest() {
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findByName(anyString())).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.getRemainderByName(anyString()));
        verify(inventoryRepository, times(1)).findByName(anyString());
    }

    @Test
    public void deleteRemainderById_SuccessfulTest() throws RemainderNotFoundException {
        Long id = 1L;
        Remainder remainder = new Remainder().setId(id).setName("name").setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findById(id)).thenReturn(optional);
        doNothing().when(inventoryRepository).deleteById(id);

        Assertions.assertEquals(remainder, inventoryService.deleteRemainderById(id));
        verify(inventoryRepository, times(1)).findById(id);
        verify(inventoryRepository, times(1)).deleteById(id);
    }
    @Test
    public void deleteRemainderById_RemainderNotFoundExceptionTest() {
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findById(anyLong())).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.deleteRemainderById(anyLong()));
        verify(inventoryRepository, times(1)).findById(anyLong());
        verify(inventoryRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void updateRemainderById_SuccessfulTest() throws RemainderNotFoundException {
        Long id = 1L;
        Remainder remainder = new Remainder().setId(id).setName("name").setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findById(id)).thenReturn(optional);
        when(inventoryRepository.save(remainder)).thenReturn(remainder);

        Assertions.assertEquals(remainder, inventoryService.updateRemainderById(id, remainder));
        verify(inventoryRepository, times(1)).findById(id);
        verify(inventoryRepository, times(1)).save(remainder);
    }
    @Test
    public void updateRemainderById_RemainderNotFoundExceptionTest() {
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findById(anyLong())).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.updateRemainderById(anyLong(), new Remainder()));
        verify(inventoryRepository, times(1)).findById(anyLong());
        verify(inventoryRepository, times(0)).save(new Remainder());
    }

    @Test
    public void updateRemainderLeftAmount_SuccessfulTest() throws RemainderNotFoundException {
        Long id = 1L;
        Remainder remainder = new Remainder().setId(id).setName("name").setLeft(1).setSold(1);
        Optional<Remainder> optional = Optional.of(remainder);
        when(inventoryRepository.findById(id)).thenReturn(optional);
        when(inventoryRepository.save(remainder)).thenReturn(remainder.setLeft(remainder.getLeft()+5));

        Assertions.assertEquals(remainder, inventoryService.updateRemainderLeftAmount(id, 5));
        verify(inventoryRepository, times(1)).findById(id);
        verify(inventoryRepository, times(1)).save(remainder);
    }
    @Test
    public void updateRemainderLeftAmount_RemainderNotFoundExceptionTest() {
        Optional<Remainder> optional = Optional.empty();
        when(inventoryRepository.findById(anyLong())).thenReturn(optional);

        Assertions.assertThrows(RemainderNotFoundException.class, () -> inventoryService.updateRemainderLeftAmount(anyLong(), 5));
        verify(inventoryRepository, times(1)).findById(anyLong());
        verify(inventoryRepository, times(0)).save(new Remainder());
    }

}
