package pl.company.foodatu.plans.domain;

import pl.company.foodatu.plans.dto.Meal;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlansFacade {

    private final DaysPlansRepository repository;

    public PlansFacade(DaysPlansRepository repository) {
        this.repository = repository;
    }

    public PlanResponse addMealToPlan(Meal meal, UserId user, LocalDate day) {

        var dayPlan = repository
                .find(user, day)
                .orElse(new DayPlan(new User(user.id()), day, new ArrayList<>()));

        dayPlan.addMeal(new PlannedMeal(meal.name(), meal.carbons(), meal.proteins(), meal.fat()));
        DayPlan savedPlan = repository.save(dayPlan);
        return new PlanResponse(getPlannedMeals(savedPlan), savedPlan.calculateKCal());
    }

    public PlanResponse getPlanForDay(UserId user, LocalDate day) {
        return repository.find(user, day)
                .map(dayPlan -> new PlanResponse(getPlannedMeals(dayPlan), dayPlan.calculateKCal()))
                .orElse(PlanResponse.empty());
    }

    private static List<PlannedMealResponse> getPlannedMeals(DayPlan dayPlan) {
        return dayPlan.plannedMeals().stream().map(i -> new PlannedMealResponse(i.getName())).toList();
    }
}