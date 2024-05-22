package com.toleyko.springboot.productservice.handler;

import com.toleyko.springboot.productservice.handlers.GlobalProductHandler;
import com.toleyko.springboot.productservice.handlers.ProductError;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
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

public class GlobalProductHandlerTest {
    private final GlobalProductHandler globalProductHandler = new GlobalProductHandler();
    private final ProductError productError = new ProductError();

    @Test
    public void handleException_ProductNotFoundExceptionTest() {
        ResponseEntity<ProductError> response = globalProductHandler.handleException(new ProductNotFoundException("Not found"));
        productError.setInfo("Not found");

        Assertions.assertEquals(productError, response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void handleException_ExceptionTest() {
        ResponseEntity<ProductError> response = globalProductHandler.handleException(new Exception("Error"));
        productError.setInfo("Error");

        Assertions.assertEquals(productError, response.getBody());
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

        ResponseEntity<Map<String, String>> response = globalProductHandler.handleValidationException(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrors, response.getBody());
    }
}
