package pl.company.foodatu.plans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class PlannedMeal {

    @Id
    private Long id;
    private String name;
    private Double carbons;
    private Double proteins;
    private Double fat;

    public PlannedMeal(String name) {
        this.name = name;
    }

    public PlannedMeal() {
    }

    public PlannedMeal(String name, Double carbons, Double proteins, Double fat) {
        this.name = name;
        this.carbons = carbons;
        this.proteins = proteins;
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public Double getKCal() {
        return (4 * proteins) + (4 * carbons) + (9 * fat);
    }
}