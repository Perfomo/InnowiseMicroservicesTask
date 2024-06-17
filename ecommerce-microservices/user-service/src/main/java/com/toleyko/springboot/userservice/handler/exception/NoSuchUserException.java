package com.toleyko.springboot.userservice.handler.exception;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String info) {
        super(info);
    }
}
