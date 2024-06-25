package com.toleyko.springboot.userservice.handler.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String info) {
        super(info);
    }
}
