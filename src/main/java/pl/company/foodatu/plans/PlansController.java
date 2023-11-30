package pl.company.foodatu.plans;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.company.foodatu.plans.domain.PlansFacade;
import pl.company.foodatu.plans.dto.Meal;
import pl.company.foodatu.plans.dto.MealId;
import pl.company.foodatu.plans.dto.PlanResponse;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;

@RequestMapping("/api/v1/")
@RestController
class PlansController {

    private PlansFacade plansFacade;

    PlansController(PlansFacade plansFacade) {
        this.plansFacade = plansFacade;
    }

    @PostMapping("/{userId}/plans/{day}/meals")
    public PlanResponse addMealToPlan(@RequestBody MealId mealId, @PathVariable String userId, @PathVariable LocalDate day) {
        return plansFacade.addMealToPlan(mealId, new UserId(userId), day);
    }

    @GetMapping("/{userId}/plans/{day}/meals")
    public PlanResponse getPlanForDay(@PathVariable String userId, @PathVariable LocalDate day) {
        return plansFacade.getPlanForDay(new UserId(userId), day);
    }
}
