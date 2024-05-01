package com.toleyko.springboot.inventoryservice.handlers;

import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalInventoryHandler {

    @ExceptionHandler
    public ResponseEntity<InventoryError> handleException(Exception e) {
        InventoryError error = new InventoryError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<InventoryError> handleException(RemainderNotFoundException e) {
        InventoryError error = new InventoryError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
