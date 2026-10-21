package com.topaz.support.application.usecase;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.repository.SupportOrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListSupportOrdersUseCase {

    private final SupportOrderRepository repository;

    public ListSupportOrdersUseCase(SupportOrderRepository repository) {
        this.repository = repository;
    }

    /** @param status filtro opcional (null = todos) */
    @Transactional(readOnly = true)
    public List<SupportOrder> execute(OrderStatus status) {
        return status == null ? repository.findAll() : repository.findByStatus(status);
    }
}
