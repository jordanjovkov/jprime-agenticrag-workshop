package io.jprime.agenticrag.videoproductionstore.web.controller;

import io.jprime.agenticrag.videoproductionstore.domain.service.CustomerService;
import io.jprime.agenticrag.videoproductionstore.web.dto.CustomerDto;
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
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operations for managing customers")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public List<CustomerDto> findAll() {
        log.info("[CustomerController] GET /api/customers");

        return customerService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a customer by ID")
    public ResponseEntity<CustomerDto> findById(@PathVariable Integer id) {
        log.info("[CustomerController] GET /api/customers/{}", id);

        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    @Operation(summary = "Get customers by name")
    public List<CustomerDto> findByName(@RequestParam String name) {
        log.info("[CustomerController] GET /api/customers/name — name: '{}'", name);

        return customerService.findByName(name);
    }

    @GetMapping("/video-card/{videoCardId}")
    @Operation(summary = "Get customers who ordered a specific video editing card")
    public List<CustomerDto> findByVideoCardId(@PathVariable Integer videoCardId) {
        log.info("[CustomerController] GET /api/customers/video-card/{}", videoCardId);

        return customerService.findByVideoCardId(videoCardId);
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto dto) {
        log.info("[CustomerController] POST /api/customers — name: '{}'", dto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer by ID")
    public ResponseEntity<CustomerDto> update(@PathVariable Integer id,
                                              @Valid @RequestBody CustomerDto dto) {
        log.info("[CustomerController] PUT /api/customers/{}", id);

        return customerService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("[CustomerController] DELETE /api/customers/{}", id);

        return customerService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
