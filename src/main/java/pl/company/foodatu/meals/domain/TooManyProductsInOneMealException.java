package pl.company.foodatu.meals.domain;

public class TooManyProductsInOneMealException extends RuntimeException{

    public TooManyProductsInOneMealException() {
        super("Meal must not contains more than " + Meal.MAX_PRODUCTS + " products!");
    }
}
