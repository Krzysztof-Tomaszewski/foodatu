package pl.company.foodatu.meals.domain;

public class MealWithZeroProductsException extends RuntimeException {

    public MealWithZeroProductsException() {
        super("Meal must contains at least one product!");
    }
}
