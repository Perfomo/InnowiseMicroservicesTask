package com.toleyko.spring.springboot.userservice.handler.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException(String info) {
        super(info);
    }
}
