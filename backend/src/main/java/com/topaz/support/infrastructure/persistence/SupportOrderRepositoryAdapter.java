package com.topaz.support.infrastructure.persistence;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.repository.SupportOrderRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Adaptador: implementa el puerto del dominio con Spring Data JPA y traduce entidad <-> modelo. */
@Repository
class SupportOrderRepositoryAdapter implements SupportOrderRepository {

    private final SupportOrderJpaRepository jpa;

    SupportOrderRepositoryAdapter(SupportOrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public SupportOrder save(SupportOrder o) {
        return toModel(jpa.save(toEntity(o)));
    }

    @Override
    public Optional<SupportOrder> findById(Long id) {
        return jpa.findById(id).map(this::toModel);
    }

    @Override
    public List<SupportOrder> findAll() {
        return jpa.findAllByOrderByCreatedAtDesc().stream().map(this::toModel).toList();
    }

    @Override
    public List<SupportOrder> findByStatus(OrderStatus status) {
        return jpa.findByStatusOrderByCreatedAtDesc(status).stream().map(this::toModel).toList();
    }

    @Override
    public boolean existsActiveByExternalReference(String externalReference) {
        List<OrderStatus> active = Arrays.stream(OrderStatus.values()).filter(OrderStatus::isActive).toList();
        return jpa.existsByExternalReferenceAndStatusIn(externalReference, active);
    }

    private SupportOrderEntity toEntity(SupportOrder o) {
        return new SupportOrderEntity(o.getId(), o.getExternalReference(), o.getCustomerId(), o.getCategory(),
                o.getDescription(), o.getChannel(), o.getPriority(), o.getStatus(), o.getCreatedAt(), o.getUpdatedAt());
    }

    private SupportOrder toModel(SupportOrderEntity e) {
        return new SupportOrder(e.getId(), e.getExternalReference(), e.getCustomerId(), e.getCategory(),
                e.getDescription(), e.getChannel(), e.getPriority(), e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
