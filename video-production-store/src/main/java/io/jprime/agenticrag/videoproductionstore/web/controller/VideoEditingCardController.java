package io.jprime.agenticrag.videoproductionstore.web.controller;

import io.jprime.agenticrag.videoproductionstore.domain.service.VideoEditingCardService;
import io.jprime.agenticrag.videoproductionstore.web.dto.VideoEditingCardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/video-editing-cards")
@Tag(name = "Video Editing Cards", description = "Operations for managing video editing cards")
public class VideoEditingCardController {

    private static final Logger log = LoggerFactory.getLogger(VideoEditingCardController.class);

    private final VideoEditingCardService videoEditingCardService;

    public VideoEditingCardController(VideoEditingCardService videoEditingCardService) {
        this.videoEditingCardService = videoEditingCardService;
    }

    @GetMapping
    @Operation(summary = "Get all video editing cards")
    public List<VideoEditingCardDto> findAll() {
        log.info("[VideoEditingCardController] GET /api/video-editing-cards");

        return videoEditingCardService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a video editing card by ID")
    public ResponseEntity<VideoEditingCardDto> findById(@PathVariable Integer id) {
        log.info("[VideoEditingCardController] GET /api/video-editing-cards/{}", id);

        return videoEditingCardService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name")
    @Operation(summary = "Get a video editing card by name")
    public ResponseEntity<VideoEditingCardDto> findByName(@RequestParam String name) {
        log.info("[VideoEditingCardController] GET /api/video-editing-cards/name — name: '{}'", name);

        return videoEditingCardService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/price")
    @Operation(summary = "Get video editing cards by price range")
    public List<VideoEditingCardDto> findByPriceRange(@RequestParam BigDecimal minPrice,
                                                      @RequestParam BigDecimal maxPrice) {
        log.info("[VideoEditingCardController] GET /api/video-editing-cards/price — minPrice: {}, maxPrice: {}",
                minPrice, maxPrice);

        return videoEditingCardService.findByPriceRange(minPrice, maxPrice);
    }

    @PostMapping
    @Operation(summary = "Create a new video editing card")
    public ResponseEntity<VideoEditingCardDto> create(@Valid @RequestBody VideoEditingCardDto dto) {
        log.info("[VideoEditingCardController] POST /api/video-editing-cards — name: '{}'", dto.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(videoEditingCardService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a video editing card by ID")
    public ResponseEntity<VideoEditingCardDto> update(@PathVariable Integer id,
                                                      @Valid @RequestBody VideoEditingCardDto dto) {
        log.info("[VideoEditingCardController] PUT /api/video-editing-cards/{}", id);

        return videoEditingCardService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a video editing card by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("[VideoEditingCardController] DELETE /api/video-editing-cards/{}", id);

        return videoEditingCardService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
