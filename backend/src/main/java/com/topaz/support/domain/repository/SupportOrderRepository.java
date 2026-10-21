package com.topaz.support.domain.repository;

import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.SupportOrder;
import java.util.List;
import java.util.Optional;

/** Puerto de salida: el dominio define el contrato, la infraestructura lo implementa. */
public interface SupportOrderRepository {
    SupportOrder save(SupportOrder order);
    Optional<SupportOrder> findById(Long id);
    List<SupportOrder> findAll();
    List<SupportOrder> findByStatus(OrderStatus status);
    boolean existsActiveByExternalReference(String externalReference);
}
