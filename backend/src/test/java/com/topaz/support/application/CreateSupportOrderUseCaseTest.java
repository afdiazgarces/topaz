package com.topaz.support.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.topaz.support.application.command.CreateSupportOrderCommand;
import com.topaz.support.application.usecase.CreateSupportOrderUseCase;
import com.topaz.support.domain.exception.DuplicateActiveOrderException;
import com.topaz.support.domain.factory.SupportOrderFactory;
import com.topaz.support.domain.model.OrderStatus;
import com.topaz.support.domain.model.Priority;
import com.topaz.support.domain.model.SupportOrder;
import com.topaz.support.domain.repository.SupportOrderRepository;
import java.time.Clock;
import org.junit.jupiter.api.Test;

class CreateSupportOrderUseCaseTest {

    private final SupportOrderRepository repository = mock(SupportOrderRepository.class);
    private final CreateSupportOrderUseCase useCase =
            new CreateSupportOrderUseCase(repository, new SupportOrderFactory(Clock.systemUTC()));
    private final CreateSupportOrderCommand command = new CreateSupportOrderCommand(
            "REF-1", "C-1", "PAYMENT", "Descripción válida larga", "MOBILE_APP", null);

    @Test
    void createsOrderOpenWithDefaultPriority() {
        when(repository.existsActiveByExternalReference("REF-1")).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        SupportOrder order = useCase.execute(command);

        assertEquals(OrderStatus.OPEN, order.getStatus());
        assertEquals(Priority.MEDIUM, order.getPriority());
    }

    @Test
    void rejectsDuplicateActiveReference() {
        when(repository.existsActiveByExternalReference("REF-1")).thenReturn(true);

        assertThrows(DuplicateActiveOrderException.class, () -> useCase.execute(command));
        verify(repository, never()).save(any());
    }
}
