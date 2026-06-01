package io.jprime.agenticrag.videoproductionstore.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StockAvailabilityDto(
        Integer id,

        @NotNull
        @Valid
        VideoEditingCardDto videoEditingCard,

        @NotNull
        @PositiveOrZero
        Integer availability
) {}
