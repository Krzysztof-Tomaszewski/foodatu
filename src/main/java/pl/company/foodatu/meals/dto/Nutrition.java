package pl.company.foodatu.meals.dto;

public record Nutrition(
        double carbons,
        double proteins,
        double fat
) {
    public Nutrition {
        if (carbons < 0 || proteins < 0 || fat < 0) {
            throw new NegativeNutritionValuesException();
        }
    }
}
