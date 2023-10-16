package pl.company.foodatu;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.company.foodatu.plans.PlansClient;
import pl.company.foodatu.plans.dto.PlanResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_CHEESE;
import static pl.company.foodatu.plans.utils.PlansTestUtils.SANDWICH_WITH_HAM;
import static pl.company.foodatu.plans.utils.PlansTestUtils.TODAY;
import static pl.company.foodatu.plans.utils.PlansTestUtils.USER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
class FoodatuApplicationTests {

	@Autowired
	PlansClient plansClient;

	@Test
	void addedMeal_then_planContains1MealAndItIsReturnedWithPlan() {
		//given
		plansClient.addMealToPlan(SANDWICH_WITH_CHEESE, USER.id(), TODAY);

		//when
		PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

		//then
		assertEquals(1, plan.plannedMeals().size());
		assertEquals(SANDWICH_WITH_CHEESE.name(), plan.plannedMeals().get(0).name());
	}

	@Test
	void added2MealsToPlan_then_shouldTellHowManyKCalInPlan() {
		//given
		plansClient.addMealToPlan(SANDWICH_WITH_CHEESE, USER.id(), TODAY);
		plansClient.addMealToPlan(SANDWICH_WITH_HAM, USER.id(), TODAY);

		//when
		PlanResponse plan = plansClient.getPlanForDay(USER.id(), TODAY);

		//then
		assertEquals(214.5 + 292.5, plan.getKCal());
	}
}
