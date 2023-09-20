package pl.company.foodatu.plans;

import org.junit.jupiter.api.Test;
import pl.company.foodatu.plans.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlansTest {

    private Meal SANDWICH = new Meal("Kanapka", 20.0, 10.0, 42.5);
    private UserId USER = new UserId("0001");
    private LocalDate TODAY = LocalDate.of(2023, 9, 20);

    private PlansFacade plansFacade = new PlansConfiguration().inMemoryFacade();

    @Test
    void shouldAddMealToDayPlan() {
        //when
        plansFacade.addMealToPlan(SANDWICH, USER, TODAY);

        //then
        var plan = plansFacade.getPlanForDay(USER, TODAY);
        List<PlannedMealResponse> plannedMeals = plan.plannedMeals();
        assertEquals(plannedMeals, List.of(new PlannedMealResponse("Kanapka")));
    }

    @Test
    void shouldNotAllowToAddMoreThan6MealsToDayPlan() {

    }

    @Test
    void shouldTellHowManyKcalInDayPlan() {

    }

}
