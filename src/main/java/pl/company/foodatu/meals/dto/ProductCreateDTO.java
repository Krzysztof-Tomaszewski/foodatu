package pl.company.foodatu.meals.dto;

import java.util.UUID;

public record ProductCreateDTO(
        UUID id,
        Double weight
) {
}
