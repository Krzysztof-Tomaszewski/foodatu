package pl.company.foodatu;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.company.foodatu.common.utils.RestResponsePage;
import pl.company.foodatu.meals.MealsClient;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.ProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;
import pl.company.foodatu.plans.PlansClient;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BREAD;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BUTTER;
import static pl.company.foodatu.meals.utils.MealsTestUtils.CHEESE;
import static pl.company.foodatu.meals.utils.MealsTestUtils.HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_CHEESE;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
class FoodatuApplicationTests {

    @Autowired
    PlansClient plansClient;
    @Autowired
    MealsClient mealsClient;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void beforeEach() throws IOException, InterruptedException {
        postgres.execInContainer(
                "psql",
                "-U", postgres.getUsername(),
                "-d", postgres.getDatabaseName(),
                "-c", "TRUNCATE users, day_plan, planned_meal, std_product, product, meal CASCADE;"
        );
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void addedStdProduct_then_stdProductListContains1StdProduct() {
        //given
        ResponseEntity<StdProductResponse> stdProductCreatedResponseEntity = mealsClient.addStdProduct(BREAD);

        //when
        ResponseEntity<RestResponsePage<StdProductResponse>> stdProductListResponseEntity = mealsClient.getStdProducts();

        //then
        assertEquals(HttpStatus.CREATED, stdProductCreatedResponseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, stdProductListResponseEntity.getStatusCode());
        assertEquals(1, stdProductListResponseEntity.getBody().getTotalElements());
        assertEquals(1, stdProductListResponseEntity.getBody().getTotalPages());
        assertEquals(1, stdProductListResponseEntity.getBody().getContent().size());
        assertEquals(BREAD.name(), stdProductListResponseEntity.getBody().getContent().get(0).name());
    }

    @Test
    void addedMeal_then_mealListContains1Meal() {
        //given
        UUID breadId = mealsClient.addStdProduct(BREAD).getBody().id();
        ResponseEntity<MealResponse> mealResponseEntity = mealsClient.addMeal(new MealCreateDTO("kanapka", List.of(new ProductCreateDTO(breadId, 30.0))));

        //when
        ResponseEntity<RestResponsePage<MealResponse>> mealListResponseEntity = mealsClient.getMeals();

        //then
        assertEquals(HttpStatus.CREATED, mealResponseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, mealListResponseEntity.getStatusCode());
        assertEquals(1, mealListResponseEntity.getBody().getTotalElements());
        assertEquals(1, mealListResponseEntity.getBody().getTotalPages());
        assertEquals(1, mealListResponseEntity.getBody().getContent().size());
        assertEquals("kanapka", mealListResponseEntity.getBody().getContent().get(0).name());
    }

    @Test
    void added0StdProducts_then_stdProductListShouldBeEmpty() {
        //when
        ResponseEntity<RestResponsePage<StdProductResponse>> stdProductListResponseEntity = mealsClient.getStdProducts();

        //then
        assertEquals(HttpStatus.OK, stdProductListResponseEntity.getStatusCode());
        assertEquals(0, stdProductListResponseEntity.getBody().getTotalElements());
        assertEquals(0, stdProductListResponseEntity.getBody().getTotalPages());
        assertEquals(0, stdProductListResponseEntity.getBody().getContent().size());
    }

    @Test
    void added9StdProducts_then_getStdProductsWithPage1AndLimit5_ShouldReturnListContaining4elements() {
        //given
        for (int i = 0; i < 9; i++) {
            mealsClient.addStdProduct(BREAD);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "5");

        //when
        ResponseEntity<RestResponsePage<StdProductResponse>> stdProductListResponseEntity = mealsClient.getStdProducts(params);

        //then
        assertEquals(HttpStatus.OK, stdProductListResponseEntity.getStatusCode());
        assertEquals(9, stdProductListResponseEntity.getBody().getTotalElements());
        assertEquals(2, stdProductListResponseEntity.getBody().getTotalPages());
        assertEquals(1, stdProductListResponseEntity.getBody().getNumber());
        assertTrue(stdProductListResponseEntity.getBody().isLast());
        assertFalse(stdProductListResponseEntity.getBody().isFirst());
        assertEquals(4, stdProductListResponseEntity.getBody().getContent().size());
    }

    @Test
    void addedStdProductWithNegativeNutritionValue_thenShouldThrowExceptionAndHaveStatusCode422() {
        //given
        String stdProduct = """
                {
                    "name": "chleb",
                    "nutritionPer100g": {
                        "carbons": -11,
                        "proteins": 11,
                        "fat": 5
                    }
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addStdProduct(stdProduct);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
    }

    @Test
    void addedStdProductWithoutName_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String stdProduct = """
                {
                    "nutritionPer100g": {
                        "carbons": 11,
                        "proteins": 11,
                        "fat": 5
                    }
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addStdProduct(stdProduct);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedMealWithoutName_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String meal = """
                {
                        "products": [{
                            "id": "a7fb1154-3fd7-4bb1-a466-7de558f50c13",
                            "weight": 20
                        }]
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedStdProductWithoutNutrition_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String stdProduct = """
                {
                    "name": "chleb"
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addStdProduct(stdProduct);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedMealWithoutProducts_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String meal = """
                {
                        "name": "kanapka"
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedMealWithEmptyProductsList_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String meal = """
                {
                        "name": "kanapka",
                        "products": []
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedMealWithNotExistingProduct_thenShouldThrowExceptionAndHaveStatusCode404() {
        //given
        String meal = """
                {
                        "name": "kanapka",
                        "products": [{
                            "id": "a7fb1154-3fd7-4bb1-a466-7de558f50c13",
                            "weight": 20
                        }]
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void addedMealWithoutProductId_thenShouldThrowExceptionAndHaveStatusCode400() {
        //given
        String meal = """
                {
                        "name": "kanapka",
                        "products": [{
                            "weight": 20
                        }]
                }
                """;
        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void addedMealWithNegativeProductWeight_thenShouldThrowExceptionAndHaveStatusCode422() {
        //given
        String productId = mealsClient.addStdProduct(BREAD).getBody().id().toString();
        String meal = String.format("""
                {
                        "name": "kanapka",
                        "products": [{
                            "id": "%s",
                            "weight": -20
                        }]
                }
                """, productId);

        //when
        Runnable runnable = () -> mealsClient.addMeal(meal);

        //then
        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, runnable::run);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
    }

    @Test
    void addedMeal_then_planContains1MealAndItIsReturnedWithPlan() {
        //given
        UUID breadId = mealsClient.addStdProduct(BREAD).getBody().id();
        MealResponse mealResponse = mealsClient.addMeal(new MealCreateDTO("kanapka",
                List.of(new ProductCreateDTO(breadId, 30.0)))).getBody();
        plansClient.addMealToPlan(new MealId(mealResponse.id()), USER.id(), TODAY);

        //when
        PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

        //then
        assertEquals(1, plan.plannedMeals().size());
        assertEquals(mealResponse.name(), plan.plannedMeals().get(0).name());
    }

    @Test
    void added2MealsToPlan_then_shouldTellHowManyKCalInPlan() {
        //given
        double epsilon = 0.000001d;
        UUID breadId = mealsClient.addStdProduct(BREAD).getBody().id();
        UUID butterId = mealsClient.addStdProduct(BUTTER).getBody().id();
        UUID cheeseId = mealsClient.addStdProduct(CHEESE).getBody().id();
        UUID hamId = mealsClient.addStdProduct(HAM).getBody().id();
        MealResponse sandwichWithHam = mealsClient.addMeal(new MealCreateDTO("Kanapka z szynka",
                List.of(new ProductCreateDTO(breadId, 50.0),
                        new ProductCreateDTO(butterId, 10.0),
                        new ProductCreateDTO(hamId, 30.0)))).getBody();

        MealResponse sandwichWithCheese = mealsClient.addMeal(new MealCreateDTO("Kanapka z serem",
                List.of(new ProductCreateDTO(breadId, 50.0),
                        new ProductCreateDTO(butterId, 10.0),
                        new ProductCreateDTO(cheeseId, 30.0)))).getBody();

        plansClient.addMealToPlan(new MealId(sandwichWithCheese.id()), USER.id(), TODAY);
        plansClient.addMealToPlan(new MealId(sandwichWithHam.id()), USER.id(), TODAY);

        //when
        PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

        //then
        assertEquals(313.705 + 246.685, plan.getKCal(), epsilon);
    }
}
