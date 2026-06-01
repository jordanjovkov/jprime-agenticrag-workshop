package io.jprime.agenticrag.retriever.domain.model.videoproductionstore;

public record Customer(Integer id,
                       String name,
                       String email,
                       String phone,
                       String address,
                       String notes) {}