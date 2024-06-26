package com.toleyko.springboot.userservice.handler.exception;

public class BadUserDataException extends Exception {
    public BadUserDataException(String info) {
        super(info);
    }
}
