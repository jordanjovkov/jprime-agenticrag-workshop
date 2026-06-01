package io.jprime.agenticrag.videoproductionstore.client.dto;

public record CustomerDto(Integer id,
                          String name,
                          String email,
                          String phone,
                          String address,
                          String notes) {}
