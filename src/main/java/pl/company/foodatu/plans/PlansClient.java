package pl.company.foodatu.plans;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.company.foodatu.plans.dto.Meal;
import pl.company.foodatu.plans.dto.PlanResponse;

import java.time.LocalDate;

@Component
public class PlansClient {

    static final String BASE_PATH = "http://localhost:8080/api/v1";

    private RestTemplate restTemplate;

    public PlansClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public PlanResponse getPlanForDay(String userId, LocalDate day) {
        return restTemplate.getForObject(
                BASE_PATH + "/" + userId + "/plans/" + day + "/meals",
                PlanResponse.class);
    }

    public PlanResponse addMealToPlan(Meal meal, String userId, LocalDate day) {
        return restTemplate.postForObject(
                BASE_PATH + "/" + userId + "/plans/" + day + "/meals",
                meal,
                PlanResponse.class);
    }
}
