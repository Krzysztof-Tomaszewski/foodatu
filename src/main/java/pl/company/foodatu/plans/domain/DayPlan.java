package pl.company.foodatu.plans.domain;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
final class DayPlan {
    public static final int MAX_MEALS = 6;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    private LocalDate localDate;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PlannedMeal> plannedMeals;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    DayPlan(
            User user,
            LocalDate localDate,
            List<PlannedMeal> plannedMeals
    ) {
        this.user = user;
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

    public User user() {
        return user;
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
        return Objects.equals(this.user, that.user) &&
                Objects.equals(this.localDate, that.localDate) &&
                Objects.equals(this.plannedMeals, that.plannedMeals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, localDate, plannedMeals);
    }

    @Override
    public String toString() {
        return "DayPlan[" +
                "user=" + user + ", " +
                "localDate=" + localDate + ", " +
                "plannedMeals=" + plannedMeals + ']';
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Double calculateKCal() {
        return plannedMeals.stream()
                .map(PlannedMeal::calculateKCal)
                .reduce(0.0, Double::sum);
    }
}