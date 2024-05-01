package com.toleyko.springboot.productservice.excepion;

import com.toleyko.springboot.productservice.handlers.GlobalProductHandler;
import com.toleyko.springboot.productservice.handlers.ProductError;
import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
}
