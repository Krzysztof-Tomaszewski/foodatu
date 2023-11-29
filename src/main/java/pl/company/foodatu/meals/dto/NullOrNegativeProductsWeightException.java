package pl.company.foodatu.meals.dto;

public class NullOrNegativeProductsWeightException extends RuntimeException {

    public NullOrNegativeProductsWeightException() {
        super("Products weight must not be null or negative!");
    }
}
