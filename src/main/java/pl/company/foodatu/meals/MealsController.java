package pl.company.foodatu.meals;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.company.foodatu.meals.domain.MealsFacade;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.List;

@RequestMapping("/api/v1/")
@RestController
class MealsController {

    private MealsFacade mealsFacade;

    MealsController(MealsFacade mealsFacade) {
        this.mealsFacade = mealsFacade;
    }

    @GetMapping("std-products")
    ResponseEntity<List<StdProductResponse>> getStdProducts() {
        return ResponseEntity.ok(mealsFacade.getStdProducts());
    }

    @PostMapping("std-products")
    ResponseEntity<StdProductResponse> addStdProduct(@RequestBody @Valid StdProductCreateDTO stdProductCreateDTO) {
        return ResponseEntity.ok(mealsFacade.addStdProduct(stdProductCreateDTO));
    }

    @GetMapping("meals")
    ResponseEntity<List<MealResponse>> getMeals() {
        return ResponseEntity.ok(mealsFacade.getMeals());
    }

    @PostMapping("meals")
    ResponseEntity<MealResponse> addMeal(@RequestBody @Valid MealCreateDTO mealCreateDTO) {
        return ResponseEntity.ok(mealsFacade.addMeal(mealCreateDTO));
    }

}
