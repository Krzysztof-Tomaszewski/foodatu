package pl.company.foodatu.meals.infrastructure;

import pl.company.foodatu.meals.dto.MealEvent;

public interface MealPublisher {

    void publishNewMeal(MealEvent mealEvent);
}
