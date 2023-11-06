package pl.company.foodatu.meals.dto;

public class NegativeProductsWeightException extends RuntimeException {

    public NegativeProductsWeightException() {
        super("Products weight must not be negative!");
    }
}
