package pl.company.foodatu.plans.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.company.foodatu.meals.domain.MealsFacade;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.PlannedMealResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_CHEESE;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

@RunWith(MockitoJUnitRunner.class)
class PlansTest {

    @Mock
    private MealsFacade mealsFacade;

    private PlansFacade plansFacade = new PlansConfiguration().plansInMemoryFacade(mealsFacade);

    @Test
    void shouldAddMealToDayPlan() {
        //when
        plansFacade.addMealToPlan(SANDWICH_WITH_CHEESE, USER, TODAY);

        //then
        var plan = plansFacade.getPlanForDay(USER, TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem")));
    }

    @Test
    void shouldAdd2MealsToDayPlan() {
        //when
        plansFacade.addMealToPlan(SANDWICH_WITH_CHEESE, USER, TODAY);
        plansFacade.addMealToPlan(SANDWICH_WITH_HAM, USER, TODAY);

        //then
        var plan = plansFacade.getPlanForDay(USER, TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka z serem"), new PlannedMealResponse("Kanapka z szynką")));
    }

    @Test
    void shouldNotAllowToAddMoreThan6MealsToDayPlan() {
        //given
        for (int i = 0; i < 6; i++) {
            plansFacade.addMealToPlan(SANDWICH_WITH_CHEESE, USER, TODAY);
        }

        //when
        Runnable runnable = () -> plansFacade.addMealToPlan(SANDWICH_WITH_CHEESE, USER, TODAY);


        //then
        assertThrows(TooManyMealsInDayPlanException.class, runnable::run);
        PlanResponse plan = plansFacade.getPlanForDay(USER, TODAY);
        assertEquals(6, plan.plannedMeals().size());
    }

    @Test
    void shouldTellHowManyKcalInDayPlan() {
        //given
        plansFacade.addMealToPlan(SANDWICH_WITH_CHEESE, USER, TODAY);
        plansFacade.addMealToPlan(SANDWICH_WITH_HAM, USER, TODAY);

        //when
        Double kCal = plansFacade.getPlanForDay(USER, TODAY).getKCal();

        //then
        Assertions.assertEquals(214.5 + 292.5, kCal);
    }

}
