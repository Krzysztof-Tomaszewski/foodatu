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

    public Nutrition add(Nutrition nutrition) {
        Double carbons = carbons() + nutrition.carbons();
        Double proteins = proteins() + nutrition.proteins();
        Double fat = fat() + nutrition.fat();
        return new Nutrition(carbons, proteins, fat);
    }
}
