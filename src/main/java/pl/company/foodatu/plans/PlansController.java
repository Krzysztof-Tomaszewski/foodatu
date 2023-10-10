package pl.company.foodatu.plans;

import org.springframework.web.bind.annotation.*;
import pl.company.foodatu.plans.domain.PlansFacade;
import pl.company.foodatu.plans.dto.UserId;
import pl.company.foodatu.plans.dto.Meal;
import pl.company.foodatu.plans.dto.PlanResponse;

import java.time.LocalDate;

@RequestMapping("/api/v1/")
@RestController
public class PlansController {

    private PlansFacade plansFacade;

    public PlansController(PlansFacade plansFacade) {
        this.plansFacade = plansFacade;
    }

    @PostMapping("/{userId}/plans/{day}")
    public PlanResponse addMealToPlan(@RequestBody Meal meal, @PathVariable String userId, @PathVariable LocalDate day) {
        return plansFacade.addMealToPlan(meal, new UserId(userId), day);
    }

    @GetMapping("/{userId}/plans/{day}")
    public PlanResponse getPlanForDay(@PathVariable String userId, @PathVariable LocalDate day) {
        return plansFacade.getPlanForDay(new UserId(userId), day);
    }
}
