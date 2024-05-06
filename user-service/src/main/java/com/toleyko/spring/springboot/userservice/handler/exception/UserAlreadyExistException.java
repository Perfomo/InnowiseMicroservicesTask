package com.toleyko.spring.springboot.userservice.handler.exception;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String info) {
        super(info);
    }
}
