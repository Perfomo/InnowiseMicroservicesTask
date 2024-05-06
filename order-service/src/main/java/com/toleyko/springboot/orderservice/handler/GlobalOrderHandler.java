package com.toleyko.springboot.orderservice.handler;

import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalOrderHandler {

    @ExceptionHandler
    public ResponseEntity<OrderError> handleException(Exception e) {
        OrderError error = new OrderError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<OrderError> handleException(OrderNotFoundException e) {
        OrderError error = new OrderError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<OrderError> handleException(ForbiddenException e) {
        OrderError error = new OrderError();
        error.setInfo(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

}
