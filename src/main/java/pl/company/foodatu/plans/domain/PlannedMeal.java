package pl.company.foodatu.plans.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
class PlannedMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Double calculateKCal() {
        return (4 * proteins) + (4 * carbons) + (9 * fat);
    }
}
