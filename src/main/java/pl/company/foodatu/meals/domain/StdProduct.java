package pl.company.foodatu.meals.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;

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

    StdProduct(StdProductCreateDTO stdProductCreateDTO) {
        this.name = stdProductCreateDTO.name();
        this.proteins = stdProductCreateDTO.nutritionPer100g().proteins();
        this.carbons = stdProductCreateDTO.nutritionPer100g().carbons();
        this.fat = stdProductCreateDTO.nutritionPer100g().fat();
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
