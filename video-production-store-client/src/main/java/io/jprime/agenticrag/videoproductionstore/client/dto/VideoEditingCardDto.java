package io.jprime.agenticrag.videoproductionstore.client.dto;

import java.math.BigDecimal;

public record VideoEditingCardDto(Integer id,
                                  String name,
                                  String manufacturer,
                                  String description,
                                  BigDecimal price) {}
