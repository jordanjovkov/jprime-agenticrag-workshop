package io.jprime.agenticrag.videoproductionstore.web.controller;

import io.jprime.agenticrag.videoproductionstore.domain.service.OrderService;
import io.jprime.agenticrag.videoproductionstore.web.dto.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Operations for managing orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public List<OrderDto> findAll() {
        log.info("[OrderController] GET /api/orders");

        return orderService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an order by ID")
    public ResponseEntity<OrderDto> findById(@PathVariable Integer id) {
        log.info("[OrderController] GET /api/orders/{}", id);

        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all orders for a specific customer")
    public List<OrderDto> findByCustomerId(@PathVariable Integer customerId) {
        log.info("[OrderController] GET /api/orders/customer/{}", customerId);

        return orderService.findByCustomerId(customerId);
    }

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto) {
        log.info("[OrderController] POST /api/orders — customerId: {}, videoEditingCardId: {}",
                dto.customer().id(), dto.videoEditingCard().id());

        return orderService.create(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order by ID")
    public ResponseEntity<OrderDto> update(@PathVariable Integer id,
                                           @Valid @RequestBody OrderDto dto) {
        log.info("[OrderController] PUT /api/orders/{}", id);

        return orderService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("[OrderController] DELETE /api/orders/{}", id);

        return orderService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
