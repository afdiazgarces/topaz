package com.topaz.support.api.dto;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.Priority;
import java.time.Instant;

public record SupportOrderSummaryResponse(Long id, String externalReference, String category,
                                          Priority priority, OrderStatus status, Instant createdAt) {}
