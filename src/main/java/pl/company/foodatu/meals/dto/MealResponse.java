package pl.company.foodatu.meals.dto;

import java.util.UUID;

public record MealResponse(UUID id, String name, Nutrition nutritionValues) {
}
