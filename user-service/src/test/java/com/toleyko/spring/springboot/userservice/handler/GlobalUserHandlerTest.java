package com.toleyko.spring.springboot.userservice.handler;

import com.toleyko.spring.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exception.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handler.exception.UserAlreadyExistException;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalUserHandlerTest {

    private static final GlobalUserHandler globalUserHandler = new GlobalUserHandler();
    private static final UserIncorrectData userIncorrectData = new UserIncorrectData();

    @Test
    public void handleException_noSuchUserTest() {
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
    public void handleException_ForbiddenExceptionTest() {
        ForbiddenException forbiddenException = new ForbiddenException("Access denied");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(forbiddenException);
        userIncorrectData.setInfo("Access denied");

        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void handleException_ExceptionTest() {
        Exception exception = new Exception("Exception");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(exception);
        userIncorrectData.setInfo("Exception");

        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

}
