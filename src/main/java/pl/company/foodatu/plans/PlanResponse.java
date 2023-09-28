package pl.company.foodatu.plans;

import java.util.List;

public record PlanResponse(
        List<PlannedMealResponse> plannedMeals,
        Double kCal
) {

    static PlanResponse empty() {
        return new PlanResponse(List.of(), 0.0);
    }

    Double getKCal() {
        return kCal;
    }

}
