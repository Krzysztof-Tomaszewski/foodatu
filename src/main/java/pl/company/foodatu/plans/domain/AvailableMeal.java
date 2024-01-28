package pl.company.foodatu.plans.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import pl.company.foodatu.common.config.UuidIdentifiedEntity;

import java.util.UUID;

@Document("available_meals")
class AvailableMeal extends UuidIdentifiedEntity {
    private String name;
    private Double carbons;
    private Double proteins;
    private Double fat;

    private AvailableMeal() {

    }

    AvailableMeal(UUID id, String name, Double carbons, Double proteins, Double fat) {
        this.id = id;
        this.name = name;
        this.carbons = carbons;
        this.proteins = proteins;
        this.fat = fat;
    }

    AvailableMeal(String name, Double carbons, Double proteins, Double fat) {
        this.name = name;
        this.carbons = carbons;
        this.proteins = proteins;
        this.fat = fat;
    }

    UUID getId() {
        return id;
    }
    void setId(UUID id) {
        if (this.id != null) {
            throw new UnsupportedOperationException("ID is already defined");
        }
        this.id = id;
    }

    String getName() {
        return name;
    }

    public Double getCarbons() {
        return carbons;
    }

    public Double getProteins() {
        return proteins;
    }

    public Double getFat() {
        return fat;
    }
}
