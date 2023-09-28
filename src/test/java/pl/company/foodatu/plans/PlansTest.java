package pl.company.foodatu.plans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlansTest {

    private Meal SANDWICH_WITH_CHEESE = new Meal("Kanapka z serem", 20.0, 10.0, 10.5);
    private Meal SANDWICH_WITH_HAM = new Meal("Kanapka z szynką", 25.0, 20.0, 12.5);
    private UserId USER = new UserId("0001");
    private LocalDate TODAY = LocalDate.of(2023, 9, 20);

    private PlansFacade plansFacade = new PlansConfiguration().inMemoryFacade();

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
        Assertions.assertThrows(TooManyMealsInDayPlanException.class, runnable::run);
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
