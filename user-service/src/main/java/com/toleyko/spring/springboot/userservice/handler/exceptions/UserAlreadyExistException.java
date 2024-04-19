package com.toleyko.spring.springboot.userservice.handler.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String info) {
        super(info);
    }
}
