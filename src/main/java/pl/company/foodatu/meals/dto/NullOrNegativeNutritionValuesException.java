package pl.company.foodatu.meals.dto;

public class NullOrNegativeNutritionValuesException extends RuntimeException {

    public NullOrNegativeNutritionValuesException() {
        super("Any nutrition values must not be null or negative!");
    }
}
