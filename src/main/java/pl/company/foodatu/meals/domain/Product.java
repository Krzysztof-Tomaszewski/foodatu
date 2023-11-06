package pl.company.foodatu.meals.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.UUID;

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    private StdProduct stdProduct;
    private Double weight;

    Product(StdProduct stdProduct, Double weight) {
        this.stdProduct = stdProduct;
        this.weight = weight;
    }
}
