package io.jprime.agenticrag.retriever.domain.model.videoproductionstore;

import java.math.BigDecimal;

public record VideoEditingCard(Integer id,
                               String name,
                               String manufacturer,
                               String description,
                               BigDecimal price) {}