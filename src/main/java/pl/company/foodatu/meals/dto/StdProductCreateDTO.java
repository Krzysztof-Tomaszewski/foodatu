package pl.company.foodatu.meals.dto;

public record StdProductCreateDTO(
        String name,
        Nutrition nutritionPer100g
) {
}
