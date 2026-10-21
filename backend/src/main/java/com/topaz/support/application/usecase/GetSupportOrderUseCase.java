package com.topaz.support.application.usecase;

import com.topaz.support.domain.exception.OrderNotFoundException;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.repository.SupportOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetSupportOrderUseCase {

    private final SupportOrderRepository repository;

    public GetSupportOrderUseCase(SupportOrderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public SupportOrder execute(Long id) {
        return repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}
