package pl.company.foodatu.meals.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import pl.company.foodatu.meals.dto.MealCreateDTO;

import java.util.List;
import java.util.UUID;

@Entity
class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;

    Meal (String name, List<Product> products) {
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
}
