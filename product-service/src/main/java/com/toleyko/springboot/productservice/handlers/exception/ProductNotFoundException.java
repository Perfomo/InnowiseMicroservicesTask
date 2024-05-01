package com.toleyko.springboot.productservice.handlers.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String info) {
        super(info);
    }
}
