package pl.company.foodatu.plans.domain;

import pl.company.foodatu.common.exception.ResourceNotFoundException;
import pl.company.foodatu.meals.dto.MealEvent;
import pl.company.foodatu.meals.dto.RestMealResponse;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlansFacade {

    private final DaysPlansRepository daysPlansRepository;
    private final AvailableMealsRepository availableMealsRepository;

    public PlansFacade(DaysPlansRepository daysPlansRepository, AvailableMealsRepository availableMealsRepository) {
        this.daysPlansRepository = daysPlansRepository;
        this.availableMealsRepository = availableMealsRepository;
    }

    public void addAvailableMeal(MealEvent meal) {
        availableMealsRepository.save(new AvailableMeal(meal.id().toString(), meal.name(), meal.nutritionValues().carbons(), meal.nutritionValues().proteins(), meal.nutritionValues().fat()));
    }

    public PlanResponse addMealToPlan(MealId mealId, UserId user, LocalDate day) {

        AvailableMeal meal = availableMealsRepository.findById(mealId.id().toString()).orElseThrow(() -> new ResourceNotFoundException("Could not find meal with id: " + mealId.id()));

        var dayPlan = daysPlansRepository
                .find(user, day)
                .orElse(new DayPlan(UUID.randomUUID().toString(), user.id(), day, new ArrayList<>()));

        dayPlan.addMeal(new PlannedMeal(meal.getName(), meal.getCarbons(), meal.getProteins(), meal.getFat()));
        DayPlan savedPlan = daysPlansRepository.save(dayPlan);
        return new PlanResponse(getPlannedMeals(savedPlan), savedPlan.calculateKCal());
    }

    public PlanResponse getPlanForDay(UserId user, LocalDate day) {
        return daysPlansRepository.find(user, day)
                .map(dayPlan -> new PlanResponse(getPlannedMeals(dayPlan), dayPlan.calculateKCal()))
                .orElse(PlanResponse.empty());
    }

    private static List<PlannedMealResponse> getPlannedMeals(DayPlan dayPlan) {
        return dayPlan.plannedMeals().stream().map(i -> new PlannedMealResponse(i.getName())).toList();
    }
}