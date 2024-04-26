package com.toleyko.spring.springboot.userservice.handlers.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String info) {
        super(info);
    }
}
