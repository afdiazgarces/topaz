package com.topaz.support.domain.model;

public enum OrderStatus {
    OPEN, IN_PROGRESS, RESOLVED, CANCELLED;

    /** Una orden activa es aquella que aún no está en un estado final. */
    public boolean isActive() {
        return this == OPEN || this == IN_PROGRESS;
    }
}
