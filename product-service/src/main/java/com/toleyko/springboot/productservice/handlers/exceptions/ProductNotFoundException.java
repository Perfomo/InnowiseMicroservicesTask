package com.toleyko.springboot.productservice.handlers.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String info) {
        super(info);
    }
}
