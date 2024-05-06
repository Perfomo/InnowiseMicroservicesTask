package com.toleyko.spring.springboot.userservice.handler.exception;

public class BadUserDataException extends Exception {
    public BadUserDataException(String info) {
        super(info);
    }
}
