package pl.company.foodatu.meals.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import pl.company.foodatu.meals.dto.NullOrNegativeNutritionValuesException;

import java.util.Objects;
import java.util.UUID;

@Document("std_products")
class StdProduct {

    private String id;
    private String name;
    private Double proteins; //per 100g
    private Double carbons; //per 100g
    private Double fat; //per 100g

    private StdProduct() {
    }

    StdProduct(String id, String name, Double proteins, Double carbons, Double fat) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);

        if (carbons == null || proteins == null || fat == null ||
                carbons < 0 || proteins < 0 || fat < 0) {
            throw new NullOrNegativeNutritionValuesException();
        }

        this.id = id;
        this.name = name;
        this.proteins = proteins;
        this.carbons = carbons;
        this.fat = fat;
    }

    UUID getId() {
        return UUID.fromString(this.id);
    }

    String getName() {
        return this.name;
    }

    Double getProteins() {
        return proteins;
    }

    Double getCarbons() {
        return carbons;
    }

    Double getFat() {
        return fat;
    }
}
