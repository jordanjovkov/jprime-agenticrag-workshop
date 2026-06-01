package io.jprime.agenticrag.videoproductionstore.client.dto;

public record StockAvailabilityDto(Integer id,
                                   VideoEditingCardDto videoEditingCard,
                                   Integer availability) {}
