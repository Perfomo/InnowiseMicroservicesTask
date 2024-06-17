package com.toleyko.springboot.orderservice.handler.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String info) {
        super(info);
    }
}
