package pl.company.foodatu.plans.domain;

public class TooManyMealsInDayPlanException extends RuntimeException {

    public TooManyMealsInDayPlanException() {
        super("Plan must not contains more than " + DayPlan.MAX_MEALS + " meals!");
    }
}
