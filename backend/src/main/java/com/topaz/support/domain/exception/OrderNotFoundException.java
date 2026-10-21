package com.topaz.support.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Support order " + id + " not found");
    }
}
