package com.toleyko.spring.springboot.userservice.handlers.exceptions;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String info) {
        super(info);
    }
}
