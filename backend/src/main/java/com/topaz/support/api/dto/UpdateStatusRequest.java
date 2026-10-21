package com.topaz.support.api.dto;

import com.topaz.support.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull OrderStatus status) {}
