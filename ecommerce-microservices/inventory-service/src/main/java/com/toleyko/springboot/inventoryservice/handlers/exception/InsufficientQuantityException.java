package com.toleyko.springboot.inventoryservice.handlers.exception;

public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String info) {
        super(info);
    }
}
