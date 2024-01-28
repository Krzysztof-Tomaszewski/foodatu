package pl.company.foodatu.plans.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import pl.company.foodatu.meals.domain.MealsFacade;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_CHEESE;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

class PlansTest {

    private InMemoryAvailableMealsRepository availableMealsRepository = new InMemoryAvailableMealsRepository();
    private PlansFacade plansFacade = new PlansConfiguration().plansInMemoryFacade(availableMealsRepository);

    @Test
    void shouldAddMealToDayPlan() {
        //given
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_CHEESE.id(),
                SANDWICH_WITH_CHEESE.name(),
                SANDWICH_WITH_CHEESE.nutritionValues().carbons(),
                SANDWICH_WITH_CHEESE.nutritionValues().proteins(),
                SANDWICH_WITH_CHEESE.nutritionValues().fat()
                ));

        //when
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);

        //then
        var plan = plansFacade.getPlanForDay(USER, TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem")));
    }

    @Test
    void shouldAdd2MealsToDayPlan() {
        //given
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_CHEESE.id(),
                SANDWICH_WITH_CHEESE.name(),
                SANDWICH_WITH_CHEESE.nutritionValues().carbons(),
                SANDWICH_WITH_CHEESE.nutritionValues().proteins(),
                SANDWICH_WITH_CHEESE.nutritionValues().fat()
        ));
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_HAM.id(),
                SANDWICH_WITH_HAM.name(),
                SANDWICH_WITH_HAM.nutritionValues().carbons(),
                SANDWICH_WITH_HAM.nutritionValues().proteins(),
                SANDWICH_WITH_HAM.nutritionValues().fat()
        ));

        //when
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_HAM.id()), USER, TODAY);

        //then
        var plan = plansFacade.getPlanForDay(USER, TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem"), new PlannedMealResponse("Kanapka z szynką")));
    }

    @Test
    void shouldNotAllowToAddMoreThan6MealsToDayPlan() {
        //given
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_CHEESE.id(),
                SANDWICH_WITH_CHEESE.name(),
                SANDWICH_WITH_CHEESE.nutritionValues().carbons(),
                SANDWICH_WITH_CHEESE.nutritionValues().proteins(),
                SANDWICH_WITH_CHEESE.nutritionValues().fat()
        ));
        for (int i = 0; i < 6; i++) {
            plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);
        }

        //when
        Runnable runnable = () -> plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);


        //then
        assertThrows(TooManyMealsInDayPlanException.class, runnable::run);
        PlanResponse plan = plansFacade.getPlanForDay(USER, TODAY);
        assertEquals(6, plan.plannedMeals().size());
    }

    @Test
    void shouldTellHowManyKcalInDayPlan() {
        //given
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_CHEESE.id(),
                SANDWICH_WITH_CHEESE.name(),
                SANDWICH_WITH_CHEESE.nutritionValues().carbons(),
                SANDWICH_WITH_CHEESE.nutritionValues().proteins(),
                SANDWICH_WITH_CHEESE.nutritionValues().fat()
        ));
        availableMealsRepository.save(new AvailableMeal(
                SANDWICH_WITH_HAM.id(),
                SANDWICH_WITH_HAM.name(),
                SANDWICH_WITH_HAM.nutritionValues().carbons(),
                SANDWICH_WITH_HAM.nutritionValues().proteins(),
                SANDWICH_WITH_HAM.nutritionValues().fat()
        ));
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_HAM.id()), USER, TODAY);

        //when
        Double kCal = plansFacade.getPlanForDay(USER, TODAY).getKCal();

        //then
        Assertions.assertEquals(214.5 + 292.5, kCal);
    }

}
