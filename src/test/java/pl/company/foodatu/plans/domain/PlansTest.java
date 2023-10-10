package pl.company.foodatu.plans.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;
import pl.company.foodatu.plans.utils.PlansTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlansTest {

    private PlansFacade plansFacade = new PlansConfiguration().inMemoryFacade();

    @Test
    void shouldAddMealToDayPlan() {
        //when
        plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER, PlansTestUtils.TODAY);

        //then
        var plan = plansFacade.getPlanForDay(PlansTestUtils.USER, PlansTestUtils.TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem")));
    }

    @Test
    void shouldAdd2MealsToDayPlan() {
        //when
        plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER, PlansTestUtils.TODAY);
        plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_HAM, PlansTestUtils.USER, PlansTestUtils.TODAY);

        //then
        var plan = plansFacade.getPlanForDay(PlansTestUtils.USER, PlansTestUtils.TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem"), new PlannedMealResponse("Kanapka z szynką")));
    }

    @Test
    void shouldNotAllowToAddMoreThan6MealsToDayPlan() {
        //given
        for (int i = 0; i < 6; i++) {
            plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER, PlansTestUtils.TODAY);
        }

        //when
        Runnable runnable = () -> plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER, PlansTestUtils.TODAY);


        //then
        Assertions.assertThrows(TooManyMealsInDayPlanException.class, runnable::run);
        PlanResponse plan = plansFacade.getPlanForDay(PlansTestUtils.USER, PlansTestUtils.TODAY);
        assertEquals(6, plan.plannedMeals().size());
    }

    @Test
    void shouldTellHowManyKcalInDayPlan() {
        //given
        plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER, PlansTestUtils.TODAY);
        plansFacade.addMealToPlan(PlansTestUtils.SANDWICH_WITH_HAM, PlansTestUtils.USER, PlansTestUtils.TODAY);

        //when
        Double kCal = plansFacade.getPlanForDay(PlansTestUtils.USER, PlansTestUtils.TODAY).getKCal();

        //then
        Assertions.assertEquals(214.5 + 292.5, kCal);
    }

}
