package pl.company.foodatu.meals.domain;

import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.List;

class MealsFacade {

    private final MealsRepository mealsRepository;

    public MealsFacade(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    public MealResponse addMeal(MealCreateDTO meal) {
        return null;
    }

    public StdProductResponse addStdProduct(StdProductCreateDTO stdProduct) {
        return null;
    }

    public List<MealResponse> getMeals() {
        return null;
    }

    public List<StdProductResponse> getProducts() {
        return null;
    }
}
