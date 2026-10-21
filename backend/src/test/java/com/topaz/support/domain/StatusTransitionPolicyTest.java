package com.topaz.support.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.topaz.support.domain.exception.InvalidStatusTransitionException;
import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.policy.InProgressTransitionRule;
import com.topaz.support.domain.policy.OpenTransitionRule;
import com.topaz.support.domain.policy.StatusTransitionPolicy;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatusTransitionPolicyTest {

    private final StatusTransitionPolicy policy =
            new StatusTransitionPolicy(List.of(new OpenTransitionRule(), new InProgressTransitionRule()));

    @Test
    void allowsValidTransitions() {
        assertDoesNotThrow(() -> policy.validate(OrderStatus.OPEN, OrderStatus.IN_PROGRESS));
        assertDoesNotThrow(() -> policy.validate(OrderStatus.IN_PROGRESS, OrderStatus.RESOLVED));
    }

    @Test
    void rejectsInvalidTransition() {
        assertThrows(InvalidStatusTransitionException.class,
                () -> policy.validate(OrderStatus.OPEN, OrderStatus.RESOLVED));
    }

    @Test
    void finalStatesCannotTransition() {
        assertThrows(InvalidStatusTransitionException.class,
                () -> policy.validate(OrderStatus.RESOLVED, OrderStatus.OPEN));
        assertThrows(InvalidStatusTransitionException.class,
                () -> policy.validate(OrderStatus.CANCELLED, OrderStatus.IN_PROGRESS));
    }
}
