package pl.company.foodatu.meals.domain;

import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.List;

class MealsFacade {

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

    public List<StdProductResponse> getProducts() {
        return stdProductsRepository.findAll().stream()
                .map(stdProduct -> new StdProductResponse(stdProduct.getId(), stdProduct.getName()))
                .toList();
    }

    public MealResponse addMeal(MealCreateDTO meal) {
        List<Product> products = meal.products().stream()
                .map(productCreateDTO -> {
                    StdProduct stdProduct = stdProductsRepository.findById(productCreateDTO.id()).orElseThrow(RuntimeException::new);
                    return new Product(stdProduct, productCreateDTO.weight());
                })
                .toList();
        Meal savedMeal = mealsRepository.save(new Meal(meal.name(), products));
        return new MealResponse(savedMeal.getId(), savedMeal.getName());
    }

    public List<MealResponse> getMeals() {
        return mealsRepository.findAll().stream()
                .map(meal -> new MealResponse(meal.getId(), meal.getName()))
                .toList();
    }
}
