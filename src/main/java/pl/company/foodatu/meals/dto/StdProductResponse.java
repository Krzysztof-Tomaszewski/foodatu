package pl.company.foodatu.meals.dto;

import java.util.UUID;

public record StdProductResponse(
        UUID id,
        String name
) {
}
