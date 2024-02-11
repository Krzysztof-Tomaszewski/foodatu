package pl.company.foodatu.meals.domain;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.company.foodatu.common.exception.ResourceNotFoundException;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealEvent;
import pl.company.foodatu.meals.dto.RestMealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;
import pl.company.foodatu.meals.infrastructure.MealPublisher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MealsFacade {

    private final MealsRepository mealsRepository;
    private final StdProductsRepository stdProductsRepository;
    private final MealPublisher mealPublisher;
    private final MealFactory mealFactory;

    public MealsFacade(MealsRepository mealsRepository, StdProductsRepository stdProductsRepository, MealPublisher mealPublisher) {
        this.mealsRepository = mealsRepository;
        this.stdProductsRepository = stdProductsRepository;
        this.mealPublisher = mealPublisher;
        this.mealFactory = new MealFactory();
    }

    public StdProductResponse addStdProduct(@Valid StdProductCreateDTO stdProduct) {
        StdProduct savedStdProduct = stdProductsRepository.save(new StdProduct(UUID.randomUUID().toString(), stdProduct.name(), stdProduct.nutritionPer100g().proteins(), stdProduct.nutritionPer100g().carbons(), stdProduct.nutritionPer100g().fat()));
        return new StdProductResponse(savedStdProduct.getId(), savedStdProduct.getName());
    }

    public Page<StdProductResponse> getStdProducts() {
        return getStdProducts(Pageable.unpaged());
    }

    public Page<StdProductResponse> getStdProducts(Pageable pageable) {
        return stdProductsRepository.findAll(pageable)
                .map(stdProduct -> new StdProductResponse(stdProduct.getId(), stdProduct.getName()));
    }

    public RestMealResponse addMeal(MealCreateDTO meal) {
        List<Product> products = meal.products().stream()
                .map(productCreateDTO -> {
                    StdProduct stdProduct = stdProductsRepository.findById(productCreateDTO.id().toString())
                            .orElseThrow(() -> new ResourceNotFoundException("Could not find product with id: " + productCreateDTO.id()));
                    return new Product(stdProduct, productCreateDTO.weight());
                })
                .toList();
        Meal savedMeal = mealsRepository.save(mealFactory.createMeal(meal.name(), products));
        RestMealResponse mealResponse = new RestMealResponse(savedMeal.getId(), savedMeal.getName(), savedMeal.calculateNutritionValues());
        mealPublisher.publishNewMeal(new MealEvent(mealResponse.id(), mealResponse.name(), mealResponse.nutritionValues()));
        return mealResponse;
    }

    public Page<RestMealResponse> getMeals() {
        return getMeals(Pageable.unpaged());
    }

    public Page<RestMealResponse> getMeals(Pageable pageable) {
        return mealsRepository.findAll(pageable)
                .map(meal -> new RestMealResponse(meal.getId(), meal.getName(), meal.calculateNutritionValues()));
    }

    public Optional<RestMealResponse> getMeal(UUID id) {
        return mealsRepository.findById(id.toString())
                .map(meal -> new RestMealResponse(meal.getId(), meal.getName(), meal.calculateNutritionValues()));

    }
}
