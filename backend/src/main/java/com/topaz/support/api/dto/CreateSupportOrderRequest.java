package com.topaz.support.api.dto;

import com.topaz.support.domain.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSupportOrderRequest(
        @NotBlank String externalReference,
        @NotBlank String customerId,
        @NotBlank String category,
        @NotBlank @Size(min = 10, max = 500) String description,
        @NotBlank String channel,
        Priority priority) {}
