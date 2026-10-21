package com.topaz.support.domain.policy;

import com.topaz.support.domain.model.OrderStatus;
import java.util.Set;

public class OpenTransitionRule implements StatusTransitionRule {
    @Override public OrderStatus from() { return OrderStatus.OPEN; }
    @Override public Set<OrderStatus> allowedTargets() { return Set.of(OrderStatus.IN_PROGRESS, OrderStatus.CANCELLED); }
}
