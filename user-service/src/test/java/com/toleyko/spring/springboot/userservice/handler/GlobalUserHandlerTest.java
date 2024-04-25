package com.toleyko.spring.springboot.userservice.handler;

import com.toleyko.spring.springboot.userservice.handler.exceptions.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handler.exceptions.UserAlreadyExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GlobalUserHandlerTest {

    private static final GlobalUserHandler globalUserHandler = new GlobalUserHandler();
    private static final UserIncorrectData userIncorrectData = new UserIncorrectData();

    @Test
    public void GlobalUserHandler_handleException_noSuchUserTest() {
        NoSuchUserException noSuchUserException = new NoSuchUserException("User not found");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(noSuchUserException);
        userIncorrectData.setInfo("User not found");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void GlobalUserHandler_handleException_UserAlreadyExistExceptionTest() {
        UserAlreadyExistException userAlreadyExistException = new UserAlreadyExistException("User already exists");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(userAlreadyExistException);
        userIncorrectData.setInfo("User already exists");

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void GlobalUserHandler_handleException_BadUserDataExceptionTest() {
        BadUserDataException badUserDataException = new BadUserDataException("Bad user data.");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(badUserDataException);
        userIncorrectData.setInfo("Bad user data.");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

    @Test
    public void GlobalUserHandler_handleException_ExceptionTest() {
        Exception exception = new Exception("Exception");

        ResponseEntity<UserIncorrectData> response = globalUserHandler.handleException(exception);
        userIncorrectData.setInfo("Exception");

        Assertions.assertEquals(userIncorrectData, response.getBody());
    }

}
