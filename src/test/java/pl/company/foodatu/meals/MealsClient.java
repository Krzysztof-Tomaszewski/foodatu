package pl.company.foodatu.meals;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.company.foodatu.common.utils.RestResponsePage;
import pl.company.foodatu.meals.dto.MealCreateDTO;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;
import pl.company.foodatu.meals.dto.StdProductResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MealsClient {

    static final String BASE_PATH = "http://localhost:8080/api/v1";

    private RestTemplate restTemplate;

    public MealsClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public ResponseEntity<RestResponsePage<StdProductResponse>> getStdProducts() {
        return getStdProducts(new HashMap<>());
    }

    public ResponseEntity<RestResponsePage<StdProductResponse>> getStdProducts(Map<String, String> params) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_PATH + "/std-products")
                .queryParamIfPresent("page", Optional.ofNullable(params.get("page")))
                .queryParamIfPresent("size", Optional.ofNullable(params.get("size")))
                .encode()
                .toUriString();
        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<StdProductResponse>>() {
                }, params);
    }

    public ResponseEntity<StdProductResponse> addStdProduct(StdProductCreateDTO stdProductCreateDTO) {
        HttpEntity<StdProductCreateDTO> entity = new HttpEntity<>(stdProductCreateDTO);
        return restTemplate.exchange(
                BASE_PATH + "/std-products", HttpMethod.POST, entity, StdProductResponse.class);
    }

    public ResponseEntity<RestResponsePage<MealResponse>> getMeals() {
        return getMeals(new HashMap<>());
    }

    public ResponseEntity<RestResponsePage<MealResponse>> getMeals(Map<String, String> params) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_PATH + "/meals")
                .queryParamIfPresent("page", Optional.ofNullable(params.get("page")))
                .queryParamIfPresent("size", Optional.ofNullable(params.get("size")))
                .encode()
                .toUriString();
        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, null, new ParameterizedTypeReference<RestResponsePage<MealResponse>>() {
                }, params);
    }

    public ResponseEntity<MealResponse> addMeal(MealCreateDTO mealCreateDTO) {
        HttpEntity<MealCreateDTO> entity = new HttpEntity<>(mealCreateDTO);
        return restTemplate.exchange(
                BASE_PATH + "/meals", HttpMethod.POST, entity, MealResponse.class);
    }
}
