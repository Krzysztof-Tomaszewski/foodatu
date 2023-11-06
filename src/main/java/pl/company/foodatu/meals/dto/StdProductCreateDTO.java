package pl.company.foodatu.meals.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StdProductCreateDTO(

        @NotEmpty
        String name,
        @NotNull
        Nutrition nutritionPer100g
) {
}
