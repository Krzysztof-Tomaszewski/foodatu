package pl.company.foodatu.meals;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

@RequestMapping("/api/v1/")
@RestController
class MealsController {

    private MealsFacade mealsFacade;

    MealsController(MealsFacade mealsFacade) {
        this.mealsFacade = mealsFacade;
    }

    @GetMapping("std-products")
    ResponseEntity<Page<StdProductResponse>> getStdProducts(Pageable pageable) {
        return ResponseEntity.ok(mealsFacade.getStdProducts(pageable));
    }

    @PostMapping("std-products")
    ResponseEntity<StdProductResponse> addStdProduct(@RequestBody @Valid StdProductCreateDTO stdProductCreateDTO) {
        return new ResponseEntity<>(mealsFacade.addStdProduct(stdProductCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("meals")
    ResponseEntity<Page<MealResponse>> getMeals(Pageable pageable) {
        return ResponseEntity.ok(mealsFacade.getMeals(pageable));
    }

    @PostMapping("meals")
    ResponseEntity<MealResponse> addMeal(@RequestBody @Valid MealCreateDTO mealCreateDTO) {
        return new ResponseEntity<>(mealsFacade.addMeal(mealCreateDTO), HttpStatus.CREATED);
    }

}
