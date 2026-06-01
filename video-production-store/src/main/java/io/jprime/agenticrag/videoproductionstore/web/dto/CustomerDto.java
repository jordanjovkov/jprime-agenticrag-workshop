package io.jprime.agenticrag.videoproductionstore.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerDto(
        Integer id,

        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(max = 20)
        String phone,

        @NotBlank
        @Size(max = 250)
        String address,

        @Size(max = 500)
        String notes
) {}
