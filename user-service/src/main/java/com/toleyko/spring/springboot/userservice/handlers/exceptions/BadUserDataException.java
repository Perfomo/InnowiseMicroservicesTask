package com.toleyko.spring.springboot.userservice.handlers.exceptions;

public class BadUserDataException extends Exception {
    public BadUserDataException(String info) {
        super(info);
    }
}
