package io.jprime.agenticrag.videoproductionstore.client.dto;

import java.time.LocalDate;

public record OrderDto(Integer id,
                       CustomerDto customer,
                       VideoEditingCardDto videoEditingCard,
                       LocalDate orderDate,
                       String orderNote) {}
