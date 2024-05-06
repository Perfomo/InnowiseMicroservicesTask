package com.toleyko.spring.springboot.userservice.handler;

import com.toleyko.spring.springboot.userservice.handler.exception.BadUserDataException;
import com.toleyko.spring.springboot.userservice.handler.exception.NoSuchUserException;
import com.toleyko.spring.springboot.userservice.handler.exception.UserAlreadyExistException;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalUserHandler {

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(NoSuchUserException noSuchUserException) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(noSuchUserException.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(UserAlreadyExistException userAlreadyExistException) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(userAlreadyExistException.getMessage());
        return new ResponseEntity<>(data, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(BadUserDataException badUserDataException) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(badUserDataException.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(ForbiddenException exception) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(Exception exception) {
        UserIncorrectData data = new UserIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
