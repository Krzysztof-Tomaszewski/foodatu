package pl.company.foodatu.meals.dto;

public class NegativeNutritionValuesException extends RuntimeException {

    public NegativeNutritionValuesException() {
        super("Nutrition values must not be negative!");
    }
}
