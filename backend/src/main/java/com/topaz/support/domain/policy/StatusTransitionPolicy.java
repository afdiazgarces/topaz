package com.topaz.support.domain.policy;

import com.topaz.support.domain.exception.InvalidStatusTransitionException;
import com.topaz.support.domain.model.OrderStatus;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Aplica las reglas registradas. Un estado sin regla (RESOLVED, CANCELLED) es final.
 * Abierto a extensión: un nuevo estado solo requiere una nueva regla, sin modificar esta clase.
 */
public class StatusTransitionPolicy {

    private final Map<OrderStatus, Set<OrderStatus>> allowed;

    public StatusTransitionPolicy(List<StatusTransitionRule> rules) {
        this.allowed = rules.stream()
                .collect(Collectors.toMap(StatusTransitionRule::from, StatusTransitionRule::allowedTargets));
    }

    public void validate(OrderStatus from, OrderStatus to) {
        if (!allowed.getOrDefault(from, Set.of()).contains(to)) {
            throw new InvalidStatusTransitionException(from, to);
        }
    }
}
