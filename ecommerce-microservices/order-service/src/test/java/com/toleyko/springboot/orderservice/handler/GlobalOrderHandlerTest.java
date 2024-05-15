package com.toleyko.springboot.orderservice.handler;

import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalOrderHandlerTest {
    private final GlobalOrderHandler globalOrderHandler = new GlobalOrderHandler();
    private final OrderError orderError = new OrderError();

    @Test
    public void handleException_OrderNotFoundExceptionTest() {
        ResponseEntity<OrderError> response = globalOrderHandler.handleException(new OrderNotFoundException("Not found"));
        orderError.setInfo("Not found");

        Assertions.assertEquals(orderError, response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void handleException_ForbiddenExceptionTest() {
        ResponseEntity<OrderError> response = globalOrderHandler.handleException(new ForbiddenException("Forbidden"));
        orderError.setInfo("Forbidden");

        Assertions.assertEquals(orderError, response.getBody());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void handleException_ExceptionTest() {
        ResponseEntity<OrderError> response = globalOrderHandler.handleException(new Exception("Error"));
        orderError.setInfo("Error");

        Assertions.assertEquals(orderError, response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
