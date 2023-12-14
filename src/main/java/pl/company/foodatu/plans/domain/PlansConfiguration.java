package pl.company.foodatu.plans.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.company.foodatu.meals.domain.MealsFacade;

@Configuration
class PlansConfiguration {

    @Bean
    @Profile("local")
    PlansFacade plansInMemoryFacade(MealsFacade mealsFacade) {
        return new PlansFacade(new InMemoryDaysPlansRepository(), mealsFacade);
    }

    @Bean
    @Profile("!local")
    PlansFacade plansRealFacade(DaysPlansRepository repository, MealsFacade mealsFacade) {
        return new PlansFacade(repository, mealsFacade);
    }
}
