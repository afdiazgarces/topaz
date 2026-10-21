package com.topaz.support.domain.policy;

import com.topaz.support.domain.model.OrderStatus;
import java.util.Set;

public class InProgressTransitionRule implements StatusTransitionRule {
    @Override public OrderStatus from() { return OrderStatus.IN_PROGRESS; }
    @Override public Set<OrderStatus> allowedTargets() { return Set.of(OrderStatus.RESOLVED, OrderStatus.CANCELLED); }
}
