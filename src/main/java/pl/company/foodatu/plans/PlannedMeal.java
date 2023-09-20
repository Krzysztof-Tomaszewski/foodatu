package pl.company.foodatu.plans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
class PlannedMeal {

    @Id
    private Long id;
    private String name;

    public PlannedMeal(String name) {
        this.name = name;
    }

    public PlannedMeal() {
    }

    public String getName() {
        return name;
    }
}
