package pl.company.foodatu.plans.utils;

import pl.company.foodatu.plans.dto.Meal;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;

public class PlansTestUtils {

    public static final Meal SANDWICH_WITH_CHEESE = new Meal("Kanapka z serem", 20.0, 10.0, 10.5);
    public static final Meal SANDWICH_WITH_HAM = new Meal("Kanapka z szynką", 25.0, 20.0, 12.5);
    public static final UserId USER = new UserId("0001");
    public static final LocalDate TODAY = LocalDate.of(2023, 9, 20);
}
