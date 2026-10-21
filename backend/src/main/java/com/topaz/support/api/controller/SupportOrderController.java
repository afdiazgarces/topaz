package com.topaz.support.api.controller;

import com.topaz.support.api.dto.CreateSupportOrderRequest;
import com.topaz.support.api.dto.SupportOrderResponse;
import com.topaz.support.api.dto.SupportOrderSummaryResponse;
import com.topaz.support.api.dto.UpdateStatusRequest;
import com.topaz.support.api.mapper.SupportOrderApiMapper;
import com.topaz.support.application.usecase.ChangeOrderStatusUseCase;
import com.topaz.support.application.usecase.CreateSupportOrderUseCase;
import com.topaz.support.application.usecase.GetSupportOrderUseCase;
import com.topaz.support.application.usecase.ListSupportOrdersUseCase;
import com.topaz.support.domain.model.OrderStatus;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/support-orders")
public class SupportOrderController {

    private final CreateSupportOrderUseCase createOrder;
    private final ListSupportOrdersUseCase listOrders;
    private final GetSupportOrderUseCase getOrder;
    private final ChangeOrderStatusUseCase changeStatus;
    private final SupportOrderApiMapper mapper;

    public SupportOrderController(CreateSupportOrderUseCase createOrder, ListSupportOrdersUseCase listOrders,
                                  GetSupportOrderUseCase getOrder, ChangeOrderStatusUseCase changeStatus,
                                  SupportOrderApiMapper mapper) {
        this.createOrder = createOrder;
        this.listOrders = listOrders;
        this.getOrder = getOrder;
        this.changeStatus = changeStatus;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupportOrderResponse create(@Valid @RequestBody CreateSupportOrderRequest request) {
        return mapper.toResponse(createOrder.execute(mapper.toCommand(request)));
    }

    @GetMapping
    public List<SupportOrderSummaryResponse> list(@RequestParam(required = false) OrderStatus status) {
        return listOrders.execute(status).stream().map(mapper::toSummary).toList();
    }

    @GetMapping("/{id}")
    public SupportOrderResponse get(@PathVariable Long id) {
        return mapper.toResponse(getOrder.execute(id));
    }

    @PatchMapping("/{id}/status")
    public SupportOrderResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return mapper.toResponse(changeStatus.execute(id, request.status()));
    }
}
