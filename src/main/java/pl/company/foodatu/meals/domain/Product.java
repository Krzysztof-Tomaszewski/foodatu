package pl.company.foodatu.meals.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import pl.company.foodatu.meals.dto.NullOrNegativeProductsWeightException;
import pl.company.foodatu.meals.dto.Nutrition;

import java.util.Objects;
import java.util.UUID;

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    private StdProduct stdProduct;
    private Double weight;

    private Product() {
    }

    Product(StdProduct stdProduct, Double weight) {
        Objects.requireNonNull(stdProduct);

        if (weight == null || weight < 0) {
            throw new NullOrNegativeProductsWeightException();
        }

        this.stdProduct = stdProduct;
        this.weight = weight;
    }

    Nutrition calculateNutritionValues() {
        Double carbons =  (stdProduct.getCarbons()/100)*weight;
        Double proteins =  (stdProduct.getProteins()/100)*weight;
        Double fat =  (stdProduct.getFat()/100)*weight;
        return new Nutrition(carbons, proteins, fat);
    }
}
