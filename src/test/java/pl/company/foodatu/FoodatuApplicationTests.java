package pl.company.foodatu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.company.foodatu.plans.PlansClient;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.utils.PlansTestUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
class FoodatuApplicationTests {

	@Autowired
	PlansClient plansClient;

	@Test
	void contextLoads() { //NOSONAR
	}

	@Test
	void addedMeal_then_planContains1MealAndItIsReturnedWithPlan() {
		//given
		plansClient.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER.id(), PlansTestUtils.TODAY);

		//when
		PlanResponse plan = plansClient.getPlanForDay(PlansTestUtils.USER.id(), PlansTestUtils.TODAY);

		//then
		Assertions.assertEquals(1, plan.plannedMeals().size());
		Assertions.assertEquals(PlansTestUtils.SANDWICH_WITH_CHEESE.name(), plan.plannedMeals().get(0).name());
	}

	@Test
	void added2MealsToPlan_then_shouldTellHowManyKCalInPlan() {
		//given
		plansClient.addMealToPlan(PlansTestUtils.SANDWICH_WITH_CHEESE, PlansTestUtils.USER.id(), PlansTestUtils.TODAY);
		plansClient.addMealToPlan(PlansTestUtils.SANDWICH_WITH_HAM, PlansTestUtils.USER.id(), PlansTestUtils.TODAY);

		//when
		PlanResponse plan = plansClient.getPlanForDay(PlansTestUtils.USER.id(), PlansTestUtils.TODAY);

		//then
		Assertions.assertEquals(214.5 + 292.5, plan.getKCal());
	}
}
