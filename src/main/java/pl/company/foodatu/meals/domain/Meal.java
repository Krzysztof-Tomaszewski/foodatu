package pl.company.foodatu.meals.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import pl.company.foodatu.meals.dto.Nutrition;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document("meals")
class Meal {

    static final int MAX_PRODUCTS = 10;

    private String id;
    private String name;

    private List<Product> products;

    private Meal() {
    }

    Meal(String id, String name, List<Product> products) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(products);

        if (products.isEmpty()) {
            throw new MealWithZeroProductsException();
        }

        if (products.size() > MAX_PRODUCTS) {
            throw new TooManyProductsInOneMealException();
        }

        this.id = id;
        this.name = name;
        this.products = products;
    }

    UUID getId() {
        return UUID.fromString(this.id);
    }

    String getName() {
        return this.name;
    }

    Nutrition calculateNutritionValues() {
        return products.stream()
                .map(Product::calculateNutritionValues)
                .reduce(new Nutrition(0.0, 0.0, 0.0),
                        Nutrition::add);
    }
}
