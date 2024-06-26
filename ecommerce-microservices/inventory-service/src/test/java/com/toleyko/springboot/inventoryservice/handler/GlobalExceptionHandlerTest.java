package com.toleyko.springboot.inventoryservice.handler;

import com.toleyko.springboot.inventoryservice.handlers.GlobalInventoryHandler;
import com.toleyko.springboot.inventoryservice.handlers.InventoryError;
import com.toleyko.springboot.inventoryservice.handlers.exception.RemainderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void handleValidationException_MethodArgumentNotValidExceptionTest() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("obj", "field1", "error1");
        FieldError fieldError2 = new FieldError("obj", "field2", "error2");
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("field1", "error1");
        expectedErrors.put("field2", "error2");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = globalInventoryHandler.handleValidationException(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrors, response.getBody());
    }
}
