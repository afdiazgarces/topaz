package com.topaz.support.application.usecase;

import com.topaz.support.domain.exception.OrderNotFoundException;
import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.policy.StatusTransitionPolicy;
import com.topaz.support.domain.repository.SupportOrderRepository;
import java.time.Clock;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeOrderStatusUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChangeOrderStatusUseCase.class);

    private final SupportOrderRepository repository;
    private final StatusTransitionPolicy policy;
    private final Clock clock;

    public ChangeOrderStatusUseCase(SupportOrderRepository repository, StatusTransitionPolicy policy, Clock clock) {
        this.repository = repository;
        this.policy = policy;
        this.clock = clock;
    }

    @Transactional
    public SupportOrder execute(Long id, OrderStatus target) {
        SupportOrder order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        OrderStatus from = order.getStatus();
        policy.validate(from, target);
        order.changeStatus(target, Instant.now(clock));
        SupportOrder saved = repository.save(order);
        log.info("Order status changed id={} {} -> {}", id, from, target);
        return saved;
    }
}
