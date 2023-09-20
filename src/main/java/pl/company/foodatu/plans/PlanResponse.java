package pl.company.foodatu.plans;

import java.util.List;

public record PlanResponse(
        List<PlannedMealResponse> plannedMeals
) {

    static PlanResponse empty() {
        return new PlanResponse(List.of());
    }

}
