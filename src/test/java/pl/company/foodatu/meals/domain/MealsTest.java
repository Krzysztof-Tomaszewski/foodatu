package pl.company.foodatu.meals.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BREAD;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BUTTER;
import static pl.company.foodatu.meals.utils.MealsTestUtils.CHEESE;
import static pl.company.foodatu.meals.utils.MealsTestUtils.HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;

@RunWith(MockitoJUnitRunner.class)
class MealsTest {

    @Mock
    private MealResponseKafkaProducer mealResponseKafkaProducer;
    private MealsFacade mealsFacade = new MealsConfiguration().mealsInMemoryFacade(mealResponseKafkaProducer);

    void setUp() {
        MockitoAnnotations.openMocks(this);
        mealsFacade = new MealsConfiguration().mealsInMemoryFacade(mealResponseKafkaProducer);
    }

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
    void shouldAdd2MealsWith3ProductsEachAndReturnListContainingTheseMeals() {
        //given
        setUp();
        double epsilon = 0.000001d;
        ArgumentCaptor<MealResponse> captor = ArgumentCaptor.forClass(MealResponse.class);
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
        Mockito.verify(mealResponseKafkaProducer).send(sandwichWithHam);
        Mockito.verify(mealResponseKafkaProducer).send(sandwichWithCheese);
        assertEquals("Kanapka z szynka", sandwichWithHam.name());
        assertEquals(25.14, sandwichWithHam.nutritionValues().carbons(), epsilon);
        assertEquals(9.925, sandwichWithHam.nutritionValues().proteins(), epsilon);
        assertEquals(11.825, sandwichWithHam.nutritionValues().fat(), epsilon);

        assertEquals("Kanapka z serem", sandwichWithCheese.name());
        assertEquals(25.476, sandwichWithCheese.nutritionValues().carbons(), epsilon);
        assertEquals(12.007, sandwichWithCheese.nutritionValues().proteins(), epsilon);
        assertEquals(18.197, sandwichWithCheese.nutritionValues().fat(), epsilon);

        assertTrue(meals.contains(sandwichWithHam));
        assertTrue(meals.contains(sandwichWithCheese));
        assertEquals(2, meals.size());
    }

    @Test
    void shouldAddMealWith3ProductsAndReturnItById() {
        //given
        setUp();
        double epsilon = 0.000001d;
        StdProductResponse breadProductResponse = mealsFacade.addStdProduct(BREAD);
        StdProductResponse butterProductResponse = mealsFacade.addStdProduct(BUTTER);
        StdProductResponse hamProductResponse = mealsFacade.addStdProduct(HAM);

        //when
        MealResponse sandwichWithHam = mealsFacade.addMeal(new MealCreateDTO("Kanapka z szynka", List.of(
                new ProductCreateDTO(breadProductResponse.id(), 50.0),
                new ProductCreateDTO(hamProductResponse.id(), 30.0),
                new ProductCreateDTO(butterProductResponse.id(), 10.0))));
        MealResponse mealResponse = mealsFacade.getMeal(sandwichWithHam.id()).orElseThrow(ResourceNotFoundException::new);

        //then
        Mockito.verify(mealResponseKafkaProducer).send(sandwichWithHam);
        assertEquals("Kanapka z szynka", mealResponse.name());
        assertEquals(25.14, mealResponse.nutritionValues().carbons(), epsilon);
        assertEquals(9.925, mealResponse.nutritionValues().proteins(), epsilon);
        assertEquals(11.825, mealResponse.nutritionValues().fat(), epsilon);
        assertEquals(sandwichWithHam, mealResponse);
    }

    @Test
    void getNotExistingInDbMeal_shouldReturnEmptyOptional() {
        Optional<MealResponse> mealResponse = mealsFacade.getMeal(SANDWICH_WITH_HAM.id());
        assertTrue(mealResponse.isEmpty());
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
