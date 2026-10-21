package com.topaz.support.domain.model;

import java.time.Instant;

/** Modelo de dominio puro: no conoce HTTP ni JPA. */
public class SupportOrder {

    private final Long id;
    private final String externalReference;
    private final String customerId;
    private final String category;
    private final String description;
    private final String channel;
    private final Priority priority;
    private final Instant createdAt;
    private OrderStatus status;
    private Instant updatedAt;

    public SupportOrder(Long id, String externalReference, String customerId, String category,
                        String description, String channel, Priority priority, OrderStatus status,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.externalReference = externalReference;
        this.customerId = customerId;
        this.category = category;
        this.description = description;
        this.channel = channel;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void changeStatus(OrderStatus target, Instant now) {
        this.status = target;
        this.updatedAt = now;
    }

    public Long getId() { return id; }
    public String getExternalReference() { return externalReference; }
    public String getCustomerId() { return customerId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getChannel() { return channel; }
    public Priority getPriority() { return priority; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
