package pl.company.foodatu.meals.utils;

import pl.company.foodatu.meals.dto.Nutrition;
import pl.company.foodatu.meals.dto.StdProductCreateDTO;

public class MealsTestUtils {

    public static final StdProductCreateDTO BREAD = new StdProductCreateDTO("Chleb", new Nutrition(49.42, 8.85, 3.33));
    public static final StdProductCreateDTO BUTTER = new StdProductCreateDTO("Maslo", new Nutrition(1.0, 1.0, 83.0));
    public static final StdProductCreateDTO HAM = new StdProductCreateDTO("Szynka", new Nutrition(1.1, 18.0, 6.2));
    public static final StdProductCreateDTO CHEESE = new StdProductCreateDTO("Ser", new Nutrition(2.22, 24.94, 27.44));


}
