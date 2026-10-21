package com.topaz.support.application.usecase;

import com.topaz.support.application.command.CreateSupportOrderCommand;
import com.topaz.support.domain.exception.DuplicateActiveOrderException;
import com.topaz.support.domain.factory.SupportOrderFactory;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.repository.SupportOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateSupportOrderUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateSupportOrderUseCase.class);

    private final SupportOrderRepository repository;
    private final SupportOrderFactory factory;

    public CreateSupportOrderUseCase(SupportOrderRepository repository, SupportOrderFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Transactional
    public SupportOrder execute(CreateSupportOrderCommand c) {
        if (repository.existsActiveByExternalReference(c.externalReference())) {
            throw new DuplicateActiveOrderException(c.externalReference());
        }
        SupportOrder saved = repository.save(factory.create(c.externalReference(), c.customerId(),
                c.category(), c.description(), c.channel(), c.priority()));
        log.info("Order created id={} ref={} priority={}", saved.getId(), saved.getExternalReference(), saved.getPriority());
        return saved;
    }
}
