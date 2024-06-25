package com.toleyko.springboot.userservice.handler;

import com.toleyko.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.springboot.userservice.handler.exception.NoSuchUserException;
import com.toleyko.springboot.userservice.handler.exception.UserAlreadyExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalUserHandlerTest {

    private static final GlobalUserHandler globalUserHandler = new GlobalUserHandler();
    private static final UserIncorrectData userIncorrectData = new UserIncorrectData();

    @Test
    public void handleException_AccessDeniedExceptionTest() {
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access denied");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(accessDeniedException);
        userIncorrectData.setInfo("Access denied");

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void handleException_NoSuchUserExceptionTest() {
        NoSuchUserException noSuchUserException = new NoSuchUserException("User not found");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(noSuchUserException);
        userIncorrectData.setInfo("User not found");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void handleException_UserAlreadyExistExceptionTest() {
        UserAlreadyExistException userAlreadyExistException = new UserAlreadyExistException("User already exists");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(userAlreadyExistException);
        userIncorrectData.setInfo("User already exists");

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void handleException_BadUserDataExceptionTest() {
        BadUserDataException badUserDataException = new BadUserDataException("Bad user data.");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(badUserDataException);
        userIncorrectData.setInfo("Bad user data.");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void handleException_ExceptionTest() {
        Exception exception = new Exception("Exception");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(exception);
        userIncorrectData.setInfo("Exception");

        Assertions.assertEquals(userIncorrectData, response.getBody());
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

        ResponseEntity<Map<String, String>> response = globalUserHandler.handleValidationException(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(expectedErrors, response.getBody());
    }
}
