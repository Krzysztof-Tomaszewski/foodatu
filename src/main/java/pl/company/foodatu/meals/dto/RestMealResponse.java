package pl.company.foodatu.meals.dto;

import java.util.UUID;

public record RestMealResponse(UUID id, String name, Nutrition nutritionValues) {
}
