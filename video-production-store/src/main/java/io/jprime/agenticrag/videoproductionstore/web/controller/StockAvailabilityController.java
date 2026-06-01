package io.jprime.agenticrag.videoproductionstore.web.controller;

import io.jprime.agenticrag.videoproductionstore.domain.service.StockAvailabilityService;
import io.jprime.agenticrag.videoproductionstore.web.dto.StockAvailabilityDto;
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
@RequestMapping("/api/stock-availabilities")
@Tag(name = "Stock Availabilities", description = "Operations for managing stock availability")
public class StockAvailabilityController {

    private static final Logger log = LoggerFactory.getLogger(StockAvailabilityController.class);

    private final StockAvailabilityService stockAvailabilityService;

    public StockAvailabilityController(StockAvailabilityService stockAvailabilityService) {
        this.stockAvailabilityService = stockAvailabilityService;
    }

    @GetMapping
    @Operation(summary = "Get all stock availabilities")
    public List<StockAvailabilityDto> findAll() {
        log.info("[StockAvailabilityController] GET /api/stock-availabilities");

        return stockAvailabilityService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get stock availability by ID")
    public ResponseEntity<StockAvailabilityDto> findById(@PathVariable Integer id) {
        log.info("[StockAvailabilityController] GET /api/stock-availabilities/{}", id);

        return stockAvailabilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/video-card/{videoCardId}")
    @Operation(summary = "Get stock availability for a specific video editing card")
    public ResponseEntity<StockAvailabilityDto> findByVideoCardId(@PathVariable Integer videoCardId) {
        log.info("[StockAvailabilityController] GET /api/stock-availabilities/video-card/{}", videoCardId);

        return stockAvailabilityService.findByVideoCardId(videoCardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/min-quantity")
    @Operation(summary = "Get stock availabilities with at least the specified quantity")
    public List<StockAvailabilityDto> findByMinQuantity(@RequestParam int minQuantity) {
        log.info("[StockAvailabilityController] GET /api/stock-availabilities/min-quantity — minQuantity: {}",
                minQuantity);

        return stockAvailabilityService.findByMinQuantity(minQuantity);
    }

    @PostMapping
    @Operation(summary = "Create a new stock availability entry")
    public ResponseEntity<StockAvailabilityDto> create(@Valid @RequestBody StockAvailabilityDto dto) {
        log.info("[StockAvailabilityController] POST /api/stock-availabilities — videoEditingCardId: {}, availability: {}",
                dto.videoEditingCard().id(), dto.availability());

        return stockAvailabilityService.create(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a stock availability entry by ID")
    public ResponseEntity<StockAvailabilityDto> update(@PathVariable Integer id,
                                                       @Valid @RequestBody StockAvailabilityDto dto) {
        log.info("[StockAvailabilityController] PUT /api/stock-availabilities/{}", id);

        return stockAvailabilityService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a stock availability entry by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("[StockAvailabilityController] DELETE /api/stock-availabilities/{}", id);

        return stockAvailabilityService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
