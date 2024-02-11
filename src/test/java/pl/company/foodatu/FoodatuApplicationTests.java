package pl.company.foodatu;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.awaitility.Awaitility;
import org.bson.Document;
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
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import pl.company.foodatu.common.utils.RestResponsePage;
import pl.company.foodatu.meals.MealsClient;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.RestMealResponse;
import pl.company.foodatu.meals.dto.ProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;
import pl.company.foodatu.plans.PlansClient;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BREAD;
import static pl.company.foodatu.meals.utils.MealsTestUtils.BUTTER;
import static pl.company.foodatu.meals.utils.MealsTestUtils.CHEESE;
import static pl.company.foodatu.meals.utils.MealsTestUtils.HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
class FoodatuApplicationTests {

    @Autowired
    PlansClient plansClient;
    @Autowired
    MealsClient mealsClient;


    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);
    static MongoClient mongoClient;

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        kafkaContainer.start();
    }

    @BeforeEach
    void beforeEach() {
        List<String> collections = List.of("meals", "std_products", "day_plans", "available_meals");

        MongoDatabase database = mongoClient.getDatabase("test");
        for (String collection : collections) {
            database.getCollection(collection).deleteMany(new Document());
        }
    }

    @AfterAll
    static void afterAll() {
        kafkaContainer.stop();
        mongoClient.close();
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.database", () -> "test");
        registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);

        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
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
        ResponseEntity<RestMealResponse> mealResponseEntity = mealsClient.addMeal(new MealCreateDTO("kanapka", List.of(new ProductCreateDTO(breadId, 30.0))));

        //when
        ResponseEntity<RestResponsePage<RestMealResponse>> mealListResponseEntity = mealsClient.getMeals();

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
        String mealName = "kanapka";
        addMealToPlanForToday(mealName, Map.of(breadId, 30.0));

        //when
        PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

        //then
        assertEquals(1, plan.plannedMeals().size());
        assertEquals(mealName, plan.plannedMeals().get(0).name());
    }

    @Test
    void added2MealsToPlan_then_shouldTellHowManyKCalInPlan() {
        //given
        UUID breadId = mealsClient.addStdProduct(BREAD).getBody().id();
        UUID butterId = mealsClient.addStdProduct(BUTTER).getBody().id();
        UUID cheeseId = mealsClient.addStdProduct(CHEESE).getBody().id();
        UUID hamId = mealsClient.addStdProduct(HAM).getBody().id();

        addMealToPlanForToday("Kanapka z szynka", Map.of(breadId, 50.0, butterId, 10.0, hamId, 30.0));
        addMealToPlanForToday("Kanapka z serem", Map.of(breadId, 50.0, butterId, 10.0, cheeseId, 30.0));

        //when
        PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

        //then
        assertKCal(313.705 + 246.685, plan.getKCal());
    }

    void addMealToPlanForToday(String mealName, Map<UUID, Double> products) {
        RestMealResponse mealResponse = mealsClient.addMeal(new MealCreateDTO(mealName,
                products.entrySet().stream()
                        .map(product -> new ProductCreateDTO(product.getKey(), product.getValue()))
                        .toList()
        )).getBody();
        Awaitility.await().atMost(3, TimeUnit.SECONDS)
                .until(() -> {
                    plansClient.addMealToPlan(new MealId(mealResponse.id()), USER.id(), TODAY);
                    return true;
                });
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void assertKCal(double expected, double actual) {
        double epsilon = 0.000001d;
        assertEquals(expected, actual, epsilon);
    }
}
