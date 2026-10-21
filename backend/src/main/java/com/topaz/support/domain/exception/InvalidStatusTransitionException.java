package com.topaz.support.domain.exception;

import com.topaz.support.domain.model.OrderStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(OrderStatus from, OrderStatus to) {
        super("Transition " + from + " -> " + to + " is not allowed");
    }
}
