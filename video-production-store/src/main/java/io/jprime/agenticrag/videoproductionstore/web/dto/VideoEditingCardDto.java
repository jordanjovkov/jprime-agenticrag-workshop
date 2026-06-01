package io.jprime.agenticrag.videoproductionstore.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VideoEditingCardDto(
        Integer id,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 100)
        String manufacturer,

        @NotBlank
        @Size(max = 500)
        String description,

        @NotNull
        @Positive
        BigDecimal price
) {}
