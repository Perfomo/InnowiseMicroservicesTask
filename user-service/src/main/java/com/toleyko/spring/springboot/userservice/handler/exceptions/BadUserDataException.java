package com.toleyko.spring.springboot.userservice.handler.exceptions;

public class BadUserDataException extends Exception {
    public BadUserDataException(String info) {
        super(info);
    }
}
