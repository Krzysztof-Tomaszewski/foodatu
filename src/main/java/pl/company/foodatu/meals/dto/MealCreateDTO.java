package pl.company.foodatu.meals.dto;

import java.util.List;

public record MealCreateDTO(
        String name,
        List<ProductCreateDTO> products
) {
}
