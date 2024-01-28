package pl.company.foodatu.meals.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import pl.company.foodatu.common.config.UuidIdentifiedEntity;
import pl.company.foodatu.meals.dto.NullOrNegativeNutritionValuesException;

import java.util.UUID;

@Document("std_products")
class StdProduct extends UuidIdentifiedEntity {

    private String name;
    private Double proteins; //per 100g
    private Double carbons; //per 100g
    private Double fat; //per 100g

    private StdProduct() {
    }

    StdProduct(String name, Double proteins, Double carbons, Double fat) {

        if (carbons == null || proteins == null  || fat == null  ||
                carbons < 0 || proteins < 0 || fat < 0) {
            throw new NullOrNegativeNutritionValuesException();
        }

        this.name = name;
        this.proteins = proteins;
        this.carbons = carbons;
        this.fat = fat;
    }


    void setId(UUID id) {
        if (this.id != null) {
            throw new UnsupportedOperationException("ID is already defined");
        }
        this.id = id;
    }

    UUID getId() {
        return this.id;
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
