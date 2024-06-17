package com.toleyko.springboot.orderservice.handler.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException(String info) {
        super(info);
    }
}
