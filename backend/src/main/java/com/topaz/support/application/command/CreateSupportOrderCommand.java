package com.topaz.support.application.command;

import com.topaz.support.domain.model.Priority;

public record CreateSupportOrderCommand(String externalReference, String customerId, String category,
                                        String description, String channel, Priority priority) {}
