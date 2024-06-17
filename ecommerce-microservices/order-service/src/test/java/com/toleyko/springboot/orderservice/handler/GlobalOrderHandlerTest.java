package com.toleyko.springboot.orderservice.handler;

import com.toleyko.springboot.orderservice.handler.exception.ForbiddenException;
import com.toleyko.springboot.orderservice.handler.exception.OrderNotFoundException;
import com.toleyko.springboot.orderservice.handler.exception.TokenDataExtractionException;
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
    public void handleException_TokenDataExtractionExceptionTest() {
        ResponseEntity<OrderError> response = globalOrderHandler.handleException(new TokenDataExtractionException("Token"));
        orderError.setInfo("Token");

        Assertions.assertEquals(orderError, response.getBody());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void handleException_ExceptionTest() {
        ResponseEntity<OrderError> response = globalOrderHandler.handleException(new Exception("Error"));
        orderError.setInfo("Error");

        Assertions.assertEquals(orderError, response.getBody());
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

        ResponseEntity<Map<String, String>> response = globalOrderHandler.handleValidationException(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrors, response.getBody());
    }
}
