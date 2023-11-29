package pl.company.foodatu.meals.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pl.company.foodatu.meals.dto.NullOrNegativeNutritionValuesException;

import java.util.UUID;

@Entity
class StdProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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
        this.id = id;
    }

    UUID getId() {
        return this.id;
    }

    String getName() {
        return this.name;
    }
}
