package pl.company.foodatu.plans.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Document("available_meals")
public class AvailableMeal {
    private String id;
    private String name;
    private Double carbons;
    private Double proteins;
    private Double fat;

    private AvailableMeal() {

    }

    AvailableMeal(String id, String name, Double carbons, Double proteins, Double fat) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(carbons);
        Objects.requireNonNull(proteins);
        Objects.requireNonNull(fat);

        this.id = id;
        this.name = name;
        this.carbons = carbons;
        this.proteins = proteins;
        this.fat = fat;
    }

    public UUID getId() {
        return UUID.fromString(this.id);
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
