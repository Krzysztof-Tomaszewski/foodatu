package pl.company.foodatu.plans.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Document("day_plans")
final class DayPlan {
    public static final int MAX_MEALS = 6;

    private String id;
    private String userId;
    private LocalDate localDate;

    private List<PlannedMeal> plannedMeals;

    DayPlan(
            String id,
            String userId,
            LocalDate localDate,
            List<PlannedMeal> plannedMeals
    ) {
        this.id = id;
        this.userId = userId;
        this.localDate = localDate;
        this.plannedMeals = plannedMeals;
    }

    public DayPlan() {
    }

    DayPlan addMeal(PlannedMeal plannedMeal) {

        if (plannedMeals().size() >= MAX_MEALS) {
            throw new TooManyMealsInDayPlanException();
        }

        plannedMeals.add(plannedMeal);
        return this;
    }

    public String userId() {
        return userId;
    }

    public LocalDate localDate() {
        return localDate;
    }

    public List<PlannedMeal> plannedMeals() {
        return plannedMeals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DayPlan) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.localDate, that.localDate) &&
                Objects.equals(this.plannedMeals, that.plannedMeals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, localDate, plannedMeals);
    }

    @Override
    public String toString() {
        return "DayPlan[" +
                "id=" + id + ", " +
                "userId=" + userId + ", " +
                "localDate=" + localDate + ", " +
                "plannedMeals=" + plannedMeals + ']';
    }

    public Double calculateKCal() {
        return plannedMeals.stream()
                .map(PlannedMeal::calculateKCal)
                .reduce(0.0, Double::sum);
    }
}