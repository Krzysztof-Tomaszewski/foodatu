package pl.company.foodatu.plans.domain;

import pl.company.foodatu.common.exception.ResourceNotFoundException;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlansFacade {

    private final DaysPlansRepository repository;
    private final AvailableMealsRepository availableMealsRepository;

    public PlansFacade(DaysPlansRepository repository, AvailableMealsRepository availableMealsRepository) {
        this.repository = repository;
        this.availableMealsRepository = availableMealsRepository;
    }

    public PlanResponse addMealToPlan(MealId mealId, UserId user, LocalDate day) {

        AvailableMeal meal = availableMealsRepository.findById(mealId.id()).orElseThrow(() -> new ResourceNotFoundException("Could not find meal with id: " + mealId.id()));

        var dayPlan = repository
                .find(user, day)
                .orElse(new DayPlan(user.id(), day, new ArrayList<>()));

        dayPlan.addMeal(new PlannedMeal(meal.getName(), meal.getCarbons(), meal.getProteins(), meal.getFat()));
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