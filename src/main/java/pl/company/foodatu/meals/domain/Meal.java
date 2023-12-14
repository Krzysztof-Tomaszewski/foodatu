package pl.company.foodatu.meals.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import pl.company.foodatu.meals.dto.Nutrition;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
class Meal {

    static final int MAX_PRODUCTS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    private Meal() {
    }

    Meal(String name, List<Product> products) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(products);

        if (products.isEmpty()) {
            throw new MealWithZeroProductsException();
        }

        if (products.size() > MAX_PRODUCTS) {
            throw new TooManyProductsInOneMealException();
        }

        this.name = name;
        this.products = products;
    }

    void setId(UUID id) {
        this.id = id;
    }

    UUID getId() {
        return this.id;
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
