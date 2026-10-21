package com.topaz.support.api.dto;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.Priority;
import java.time.Instant;

public record SupportOrderResponse(Long id, String externalReference, String customerId, String category,
                                   String description, String channel, Priority priority, OrderStatus status,
                                   Instant createdAt, Instant updatedAt) {}
