package com.topaz.support.infrastructure.persistence;

import com.topaz.support.domain.model.OrderStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SupportOrderJpaRepository extends JpaRepository<SupportOrderEntity, Long> {
    boolean existsByExternalReferenceAndStatusIn(String externalReference, Collection<OrderStatus> statuses);
    List<SupportOrderEntity> findAllByOrderByCreatedAtDesc();
    List<SupportOrderEntity> findByStatusOrderByCreatedAtDesc(OrderStatus status);
}
