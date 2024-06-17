package com.toleyko.springboot.orderservice.handler.exception;

public class TokenDataExtractionException extends Exception {
    public TokenDataExtractionException(String info) {
        super(info);
    }
}
