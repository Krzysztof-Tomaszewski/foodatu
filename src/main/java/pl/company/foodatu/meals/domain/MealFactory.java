package pl.company.foodatu.meals.domain;

import java.util.List;
import java.util.UUID;

class MealFactory {

    Meal createMeal(String name, List<Product> products) {
        return new Meal(
                UUID.randomUUID().toString(),
                name,
                products
        );
    }
}
