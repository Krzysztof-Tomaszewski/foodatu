package pl.company.foodatu.meals.dto;

import java.util.UUID;

public record MealEvent(UUID id, String name, Nutrition nutritionValues) {
    public MealEvent(RestMealResponse mealResponse) {
        this(mealResponse.id(), mealResponse.name(), mealResponse.nutritionValues());
    }
}
