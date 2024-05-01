package com.toleyko.springboot.inventoryservice.exception;

import com.toleyko.springboot.inventoryservice.handlers.GlobalInventoryHandler;
import com.toleyko.springboot.inventoryservice.handlers.InventoryError;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandlerTest {
    private final GlobalInventoryHandler globalInventoryHandler = new GlobalInventoryHandler();
    private final InventoryError inventoryError = new InventoryError();

    @Test
    public void handleException_RemainderNotFoundExceptionTest() {
        ResponseEntity<InventoryError> response = globalInventoryHandler.handleException(new RemainderNotFoundException("Not found"));
        inventoryError.setInfo("Not found");

        Assertions.assertEquals(inventoryError, response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void handleException_ExceptionTest() {
        ResponseEntity<InventoryError> response = globalInventoryHandler.handleException(new Exception("Error"));
        inventoryError.setInfo("Error");

        Assertions.assertEquals(inventoryError, response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
