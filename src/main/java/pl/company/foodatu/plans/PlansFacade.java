package pl.company.foodatu.plans;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlansFacade {

    private final DaysPlansRepository repository;

    public PlansFacade(DaysPlansRepository repository) {
        this.repository = repository;
    }

    public void addMealToPlan(Meal meal, UserId user, LocalDate day) {

        var dayPlan = repository
                .find(user, day)
                .orElse(new DayPlan(new User(user.id()), day, new ArrayList<>()));

        if (dayPlan.plannedMeals().size() >= 6) {
            throw new TooManyMealsInDayPlanException();
        }

        dayPlan.withMeal(new PlannedMeal(meal.name(), meal.carbons(), meal.proteins(), meal.fat()));
        repository.save(dayPlan);
    }

    public PlanResponse getPlanForDay(UserId user, LocalDate day) {
        return repository.find(user, day)
                .map(dayPlan -> new PlanResponse(getPlannedMeals(dayPlan), dayPlan.getKcal()))
                .orElse(PlanResponse.empty());
    }

    private static List<PlannedMealResponse> getPlannedMeals(DayPlan dayPlan) {
        return dayPlan.plannedMeals().stream().map(i -> new PlannedMealResponse(i.getName())).toList();
    }
}