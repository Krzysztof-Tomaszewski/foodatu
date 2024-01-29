package pl.company.foodatu.meals.infrastructure;

import pl.company.foodatu.meals.dto.MealResponse;

public interface MealPublisher {

    void publishNewMeal(MealResponse mealResponse);
}
