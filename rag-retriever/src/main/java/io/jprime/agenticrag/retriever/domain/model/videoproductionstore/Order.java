package io.jprime.agenticrag.retriever.domain.model.videoproductionstore;

import java.time.LocalDate;

public record Order(Integer id,
                    Customer customer,
                    VideoEditingCard videoEditingCard,
                    LocalDate orderDate,
                    String orderNote) {}