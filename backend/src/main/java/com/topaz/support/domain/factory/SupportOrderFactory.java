package com.topaz.support.domain.factory;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.Priority;
import com.topaz.support.domain.model.SupportOrder;
import java.time.Clock;
import java.time.Instant;

/** Factory: centraliza las reglas de creación (estado inicial OPEN, prioridad por defecto, fecha del servidor). */
public class SupportOrderFactory {

    private final Clock clock;

    public SupportOrderFactory(Clock clock) {
        this.clock = clock;
    }

    public SupportOrder create(String externalReference, String customerId, String category,
                               String description, String channel, Priority priority) {
        Instant now = Instant.now(clock);
        return new SupportOrder(null, externalReference, customerId, category, description, channel,
                priority == null ? Priority.MEDIUM : priority, OrderStatus.OPEN, now, now);
    }
}
