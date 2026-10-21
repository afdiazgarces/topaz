package com.topaz.support.domain.exception;

public class DuplicateActiveOrderException extends RuntimeException {
    public DuplicateActiveOrderException(String externalReference) {
        super("An active order already exists for externalReference " + externalReference);
    }
}
