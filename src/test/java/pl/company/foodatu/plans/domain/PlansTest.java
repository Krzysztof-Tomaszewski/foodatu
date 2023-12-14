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

@RunWith(MockitoJUnitRunner.class)
class PlansTest {

    @Mock
    private MealsFacade mealsFacade;

    private PlansFacade plansFacade;


    void setUp() {
        MockitoAnnotations.openMocks(this);
        plansFacade = new PlansConfiguration().plansInMemoryFacade(mealsFacade);
    }
    @Test
    void shouldAddMealToDayPlan() {
        //given
        setUp();
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_CHEESE.id())).thenReturn(Optional.of(SANDWICH_WITH_CHEESE));

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
        setUp();
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_CHEESE.id())).thenReturn(Optional.of(SANDWICH_WITH_CHEESE));
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_HAM.id())).thenReturn(Optional.of(SANDWICH_WITH_HAM));


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
        setUp();
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_CHEESE.id())).thenReturn(Optional.of(SANDWICH_WITH_CHEESE));
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
        setUp();
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_CHEESE.id())).thenReturn(Optional.of(SANDWICH_WITH_CHEESE));
        Mockito.when(mealsFacade.getMeal(SANDWICH_WITH_HAM.id())).thenReturn(Optional.of(SANDWICH_WITH_HAM));

        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_CHEESE.id()), USER, TODAY);
        plansFacade.addMealToPlan(new MealId(SANDWICH_WITH_HAM.id()), USER, TODAY);

        //when
        Double kCal = plansFacade.getPlanForDay(USER, TODAY).getKCal();

        //then
        Assertions.assertEquals(214.5 + 292.5, kCal);
    }

}
