package pl.company.foodatu.meals.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCreateDTO(
        @NotNull
        UUID id,
        @NotNull
        Double weight
) {
}
