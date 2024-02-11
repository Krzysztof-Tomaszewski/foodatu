package pl.company.foodatu.plans.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_CHEESE;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

class PlansTest {

    private final PlansFacade plansFacade = new PlansConfiguration().plansInMemoryFacade();

    @Test
    void shouldAddMealToDayPlan() {
        //given
        plansFacade.addAvailableMeal(SANDWICH_WITH_CHEESE);

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
        plansFacade.addAvailableMeal(SANDWICH_WITH_CHEESE);
        plansFacade.addAvailableMeal(SANDWICH_WITH_HAM);

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
        plansFacade.addAvailableMeal(SANDWICH_WITH_CHEESE);
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
        plansFacade.addAvailableMeal(SANDWICH_WITH_CHEESE);
        plansFacade.addAvailableMeal(SANDWICH_WITH_HAM);
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_HAM.id()), USER, TODAY);

        //when
        Double kCal = plansFacade.getPlanForDay(USER, TODAY).getKCal();

        //then
        Assertions.assertEquals(214.5 + 292.5, kCal);
    }

}
