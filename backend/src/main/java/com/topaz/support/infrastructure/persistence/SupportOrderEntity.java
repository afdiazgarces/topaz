package com.topaz.support.infrastructure.persistence;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.Priority;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "support_orders", indexes = @Index(name = "idx_support_orders_ref", columnList = "externalReference"))
public class SupportOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String externalReference;
    @Column(nullable = false) private String customerId;
    @Column(nullable = false) private String category;
    @Column(nullable = false, length = 500) private String description;
    @Column(nullable = false) private String channel;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Priority priority;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus status;
    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    protected SupportOrderEntity() {}

    public SupportOrderEntity(Long id, String externalReference, String customerId, String category,
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
