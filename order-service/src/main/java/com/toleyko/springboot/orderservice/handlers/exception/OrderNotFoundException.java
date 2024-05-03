package com.toleyko.springboot.orderservice.handlers.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String info) {
        super(info);
    }
}
