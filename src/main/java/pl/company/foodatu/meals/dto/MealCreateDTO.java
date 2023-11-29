package pl.company.foodatu.meals.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MealCreateDTO(
        @NotEmpty
        String name,
        @NotEmpty
        @Valid
        List<ProductCreateDTO> products
) {
}
