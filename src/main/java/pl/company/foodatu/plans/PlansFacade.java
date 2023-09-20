package pl.company.foodatu.plans;

import java.time.LocalDate;
import java.util.List;

public class PlansFacade {

    private final DaysPlansRepository repository;

    public PlansFacade(DaysPlansRepository repository) {
        this.repository = repository;
    }

    public void addMealToPlan(Meal meal, UserId user, LocalDate day) {
        var dayPlan = repository
                .find(user, day)
                .map(it -> it.withMeal(new PlannedMeal(meal.name())))
                .orElse(new DayPlan(new User(user.id()), day, List.of(new PlannedMeal(meal.name()))));
        repository.save(dayPlan);
    }

    public PlanResponse getPlanForDay(UserId user, LocalDate day) {
        return repository.find(user, day)
                .map(it -> new PlanResponse(getPlannedMeals(it)))
                .orElse(PlanResponse.empty());
    }

    private static List<PlannedMealResponse> getPlannedMeals(DayPlan it) {
        return it.plannedMeals().stream().map(i -> new PlannedMealResponse(i.getName())).toList();
    }
}