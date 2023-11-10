package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.company.foodatu.common.exception.ResourceNotFoundException;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.List;

public class MealsFacade {

    private final MealsRepository mealsRepository;
    private final StdProductsRepository stdProductsRepository;

    public MealsFacade(MealsRepository mealsRepository, StdProductsRepository stdProductsRepository) {
        this.mealsRepository = mealsRepository;
        this.stdProductsRepository = stdProductsRepository;
    }

    public StdProductResponse addStdProduct(StdProductCreateDTO stdProduct) {
        StdProduct savedStdProduct = stdProductsRepository.save(new StdProduct(stdProduct));
        return new StdProductResponse(savedStdProduct.getId(), savedStdProduct.getName());
    }

    public Page<StdProductResponse> getStdProducts() {
        return getStdProducts(Pageable.unpaged());
    }

    public Page<StdProductResponse> getStdProducts(Pageable pageable) {
        return stdProductsRepository.findAll(pageable)
                .map(stdProduct -> new StdProductResponse(stdProduct.getId(), stdProduct.getName()));
    }

    public MealResponse addMeal(MealCreateDTO meal) {
        List<Product> products = meal.products().stream()
                .map(productCreateDTO -> {
                    StdProduct stdProduct = stdProductsRepository.findById(productCreateDTO.id())
                            .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id: " + productCreateDTO.id()));
                    return new Product(stdProduct, productCreateDTO.weight());
                })
                .toList();
        Meal savedMeal = mealsRepository.save(new Meal(meal.name(), products));
        return new MealResponse(savedMeal.getId(), savedMeal.getName());
    }

    public Page<MealResponse> getMeals() {
        return getMeals(Pageable.unpaged());
    }

    public Page<MealResponse> getMeals(Pageable pageable) {
        return mealsRepository.findAll(pageable)
                .map(meal -> new MealResponse(meal.getId(), meal.getName()));
    }
}
