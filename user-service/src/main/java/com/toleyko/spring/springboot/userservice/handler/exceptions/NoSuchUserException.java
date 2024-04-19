package com.toleyko.spring.springboot.userservice.handler.exceptions;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String info) {
        super(info);
    }
}
