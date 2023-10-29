package pl.company.foodatu.plans.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlanResponse(
        List<PlannedMealResponse> plannedMeals,
        Double kCal
) {

    public static PlanResponse empty() {
        return new PlanResponse(List.of(), 0.0);
    }

    @JsonProperty("kCal")
    public Double getKCal() {
        return kCal;
    }

}
