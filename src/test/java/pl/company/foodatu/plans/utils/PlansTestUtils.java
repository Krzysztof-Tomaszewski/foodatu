package pl.company.foodatu.plans.utils;

import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.dto.Nutrition;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.UUID;

public class PlansTestUtils {

    public static final MealResponse SANDWICH_WITH_CHEESE = new MealResponse(UUID.fromString("aaaa1154-3fd7-4bb1-a466-7de558f50c13"),
            "Kanapka z serem", new Nutrition( 20.0, 10.0, 10.5));
    public static final MealResponse SANDWICH_WITH_HAM = new MealResponse(UUID.fromString("bbbb1154-3fd7-4bb1-a466-7de558f50c13"),
            "Kanapka z szynką", new Nutrition(25.0, 20.0, 12.5));
    public static final UserId USER = new UserId("0001");
    public static final LocalDate TODAY = LocalDate.of(2023, 9, 20);
}
