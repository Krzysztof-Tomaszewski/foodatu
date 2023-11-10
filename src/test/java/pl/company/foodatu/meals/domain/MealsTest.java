package pl.company.foodatu.meals.domain;

import org.junit.jupiter.api.Test;
import pl.company.foodatu.common.exception.ResourceNotFoundException;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.NullOrNegativeNutritionValuesException;
import pl.company.foodatu.meals.dto.NullOrNegativeProductsWeightException;
import pl.company.foodatu.meals.dto.Nutrition;
import pl.company.foodatu.meals.dto.ProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BREAD;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BUTTER;
import static pl.company.foodatu.meals.utils.MealsTestUtils.CHEESE;
import static pl.company.foodatu.meals.utils.MealsTestUtils.HAM;

class MealsTest {

    private MealsFacade mealsFacade = new MealsConfiguration().mealsInMemoryFacade();

    @Test
    void shouldAdd2StdProductsAndReturnListContainingTheseProducts() {
        //when
        StdProductResponse breadProductResponse = mealsFacade.addStdProduct(BREAD);
        StdProductResponse butterProductResponse = mealsFacade.addStdProduct(BUTTER);
        List<StdProductResponse> products = mealsFacade.getStdProducts().getContent();

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
        List<MealResponse> meals = mealsFacade.getMeals().getContent();

        //then
        assertEquals("Kanapka z szynka", sandwichWithHam.name());
        assertEquals("Kanapka z serem", sandwichWithCheese.name());
        assertTrue(meals.contains(sandwichWithHam));
        assertTrue(meals.contains(sandwichWithCheese));
        assertEquals(2, meals.size());
    }

    @Test
    void shouldNotAllowToAddMealContainingMoreThan10Products() {
        //given
        List<ProductCreateDTO> productCreateDTOS = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            StdProductResponse stdProductResponse = mealsFacade.addStdProduct(BREAD);
            productCreateDTOS.add(new ProductCreateDTO(stdProductResponse.id(), 100.0));
        }

        //when
        Runnable runnable = () -> mealsFacade.addMeal(new MealCreateDTO("test", productCreateDTOS));

        //then
        assertThrows(TooManyProductsInOneMealException.class, runnable::run);
        List<MealResponse> meals = mealsFacade.getMeals().getContent();
        assertTrue(meals.isEmpty());
    }

    @Test
    void shouldNotAllowToAddMealContainingProductWithNegativeWeight() {
        //given
        StdProductResponse stdProductResponse = mealsFacade.addStdProduct(BREAD);
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(stdProductResponse.id(), -1.0);

        //when
        Runnable runnable = () -> mealsFacade.addMeal(new MealCreateDTO("test", List.of(productCreateDTO)));

        //then
        assertThrows(NullOrNegativeProductsWeightException.class, runnable::run);
        List<MealResponse> meals = mealsFacade.getMeals().getContent();
        assertTrue(meals.isEmpty());
    }

    @Test
    void shouldNotAllowToAddMealContainingProductWithoutWeight() {
        //given
        StdProductResponse stdProductResponse = mealsFacade.addStdProduct(BREAD);
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(stdProductResponse.id(), null);

        //when
        Runnable runnable = () -> mealsFacade.addMeal(new MealCreateDTO("test", List.of(productCreateDTO)));

        //then
        assertThrows(NullOrNegativeProductsWeightException.class, runnable::run);
        List<MealResponse> meals = mealsFacade.getMeals().getContent();
        assertTrue(meals.isEmpty());
    }

    @Test
    void shouldNotAllowToAddMealContainingEmptyProducts() {
        //when
        Runnable runnable = () -> mealsFacade.addMeal(new MealCreateDTO("test", new ArrayList<>()));

        //then
        assertThrows(MealWithZeroProductsException.class, runnable::run);
        List<MealResponse> meals = mealsFacade.getMeals().getContent();
        assertTrue(meals.isEmpty());
    }

    @Test
    void shouldNotAllowToAddMealContainingProductWhichDoesNotExistInDb() {
        //given
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(UUID.randomUUID(), 1.0);

        //when
        Runnable runnable = () -> mealsFacade.addMeal(new MealCreateDTO("test", List.of(productCreateDTO)));

        //then
        assertThrows(ResourceNotFoundException.class, runnable::run);
        List<MealResponse> meals = mealsFacade.getMeals().getContent();
        assertTrue(meals.isEmpty());
    }

    @Test
    void shouldNotAllowToAddStdProductWithNegativeNutrition() {
        //when
        Runnable runnable = () -> mealsFacade.addStdProduct(new StdProductCreateDTO("test", new Nutrition(1.0, 1.0, -1.0)));

        //then
        assertThrows(NullOrNegativeNutritionValuesException.class, runnable::run);
        List<StdProductResponse> stdProducts = mealsFacade.getStdProducts().getContent();
        assertTrue(stdProducts.isEmpty());
    }

    @Test
    void shouldNotAllowToAddStdProductWithNullNutritionValue() {
        //when
        Runnable runnable = () -> mealsFacade.addStdProduct(new StdProductCreateDTO("test", new Nutrition(1.0, null, 1.0)));

        //then
        assertThrows(NullOrNegativeNutritionValuesException.class, runnable::run);
        List<StdProductResponse> stdProducts = mealsFacade.getStdProducts().getContent();
        assertTrue(stdProducts.isEmpty());
    }
}
