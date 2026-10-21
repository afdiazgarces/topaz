package com.topaz.support.domain.policy;

import com.topaz.support.domain.model.OrderStatus;
import java.util.Set;

/** Strategy: cada implementación define las transiciones permitidas desde un estado. */
public interface StatusTransitionRule {
    OrderStatus from();
    Set<OrderStatus> allowedTargets();
}
