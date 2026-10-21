package com.topaz.support.api.mapper;

import com.topaz.support.api.dto.CreateSupportOrderRequest;
import com.topaz.support.api.dto.SupportOrderResponse;
import com.topaz.support.api.dto.SupportOrderSummaryResponse;
import com.topaz.support.application.command.CreateSupportOrderCommand;
import com.topaz.support.domain.model.SupportOrder;
import org.springframework.stereotype.Component;

@Component
public class SupportOrderApiMapper {

    public CreateSupportOrderCommand toCommand(CreateSupportOrderRequest r) {
        return new CreateSupportOrderCommand(r.externalReference(), r.customerId(), r.category(),
                r.description(), r.channel(), r.priority());
    }

    public SupportOrderResponse toResponse(SupportOrder o) {
        return new SupportOrderResponse(o.getId(), o.getExternalReference(), o.getCustomerId(), o.getCategory(),
                o.getDescription(), o.getChannel(), o.getPriority(), o.getStatus(), o.getCreatedAt(), o.getUpdatedAt());
    }

    public SupportOrderSummaryResponse toSummary(SupportOrder o) {
        return new SupportOrderSummaryResponse(o.getId(), o.getExternalReference(), o.getCategory(),
                o.getPriority(), o.getStatus(), o.getCreatedAt());
    }
}
