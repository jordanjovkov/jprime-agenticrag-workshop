package io.jprime.agenticrag.videoproductionstore.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record OrderDto(
        Integer id,

        @NotNull
        @Valid
        CustomerDto customer,

        @NotNull
        @Valid
        VideoEditingCardDto videoEditingCard,

        @NotNull
        LocalDate orderDate,

        @Size(max = 500)
        String orderNote
) {}
