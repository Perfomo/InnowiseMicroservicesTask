package com.toleyko.springboot.productservice.handlers;

import com.toleyko.springboot.productservice.handlers.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalProductHandler {

    @ExceptionHandler
    public ResponseEntity<ProductError> handleException(Exception e) {
        ProductError error = new ProductError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ProductError> handleException(ProductNotFoundException e) {
        ProductError error = new ProductError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
