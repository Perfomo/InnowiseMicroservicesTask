package com.toleyko.springboot.inventoryservice.handlers.exception;

public class RemainderNotFoundException extends Exception {
    public RemainderNotFoundException(String info) {
        super(info);
    }
}
