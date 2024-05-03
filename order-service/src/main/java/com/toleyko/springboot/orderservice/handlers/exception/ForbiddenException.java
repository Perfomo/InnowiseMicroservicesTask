package com.toleyko.springboot.orderservice.handlers.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException(String info) {
        super(info);
    }
}
