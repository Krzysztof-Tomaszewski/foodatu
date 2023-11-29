package pl.company.foodatu.meals.dto;

public record Nutrition(
        Double carbons,
        Double proteins,
        Double fat
) {
    public Nutrition {
        if (carbons == null || proteins == null  || fat == null  ||
                carbons < 0 || proteins < 0 || fat < 0) {
            throw new NullOrNegativeNutritionValuesException();
        }
    }
}
