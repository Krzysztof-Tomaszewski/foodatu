package pl.company.foodatu.meals.domain;

import org.junit.jupiter.api.Test;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.ProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BREAD;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BUTTER;
import static pl.company.foodatu.meals.utils.MealsTestUtils.CHEESE;
import static pl.company.foodatu.meals.utils.MealsTestUtils.HAM;

class MealsTest {

    private MealsFacade mealsFacade = new MealsConfiguration().inMemoryFacade();

    @Test
    void shouldAdd2StdProductsAndReturnListContainingTheseProducts() {
        //when
        StdProductResponse breadProductResponse = mealsFacade.addStdProduct(BREAD);
        StdProductResponse butterProductResponse = mealsFacade.addStdProduct(BUTTER);
        List<StdProductResponse> products = mealsFacade.getProducts();

        //then
        assertNotNull(breadProductResponse.id());
        assertEquals(BREAD.name(), breadProductResponse.name());
        assertNotNull(butterProductResponse.id());
        assertEquals(BUTTER.name(), butterProductResponse.name());
        assertTrue(products.contains(breadProductResponse));
        assertTrue(products.contains(butterProductResponse));
        assertEquals(2, products.size());
    }

    @Test
    void shouldAdd2MealsWith2ProductsEachAndReturnListContainingTheseMeals() {
        //given
        StdProductResponse breadProductResponse = mealsFacade.addStdProduct(BREAD);
        StdProductResponse butterProductResponse = mealsFacade.addStdProduct(BUTTER);
        StdProductResponse hamProductResponse = mealsFacade.addStdProduct(HAM);
        StdProductResponse cheeseProductResponse = mealsFacade.addStdProduct(CHEESE);

        //when
        MealResponse sandwichWithHam = mealsFacade.addMeal(new MealCreateDTO("Kanapka z szynka", List.of(
                new ProductCreateDTO(breadProductResponse.id(), 50.0),
                new ProductCreateDTO(hamProductResponse.id(), 30.0),
                new ProductCreateDTO(butterProductResponse.id(), 10.0))));
        MealResponse sandwichWithCheese = mealsFacade.addMeal(new MealCreateDTO("Kanapka z serem", List.of(
                new ProductCreateDTO(breadProductResponse.id(), 50.0),
                new ProductCreateDTO(cheeseProductResponse.id(), 30.0),
                new ProductCreateDTO(butterProductResponse.id(), 10.0))));
        List<MealResponse> meals = mealsFacade.getMeals();

        //then
        assertEquals("Kanapka z szynka", sandwichWithHam.name());
        assertEquals("Kanapka z serem", sandwichWithCheese.name());
        assertTrue(meals.contains(sandwichWithHam));
        assertTrue(meals.contains(sandwichWithCheese));
        assertEquals(2, meals.size());
    }

    @Test
    void shouldNotAllowToAddMealContainingMoreThan10Products() {

    }

    @Test
    void shouldNotAllowToAddMealContainingProductWithNegativeWeight() {

    }

    @Test
    void shouldNotAllowToAddStdProductWithNegativeNutrition() {

    }
}
