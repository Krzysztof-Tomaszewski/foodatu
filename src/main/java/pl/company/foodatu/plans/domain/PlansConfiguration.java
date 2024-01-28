package pl.company.foodatu.plans.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class PlansConfiguration {

    @Bean
    @Profile("local")
    PlansFacade plansInMemoryFacade(InMemoryAvailableMealsRepository inMemoryAvailableMealsRepository) {
        if (inMemoryAvailableMealsRepository == null)
            inMemoryAvailableMealsRepository = new InMemoryAvailableMealsRepository();
        return new PlansFacade(new InMemoryDaysPlansRepository(), inMemoryAvailableMealsRepository);
    }

    @Bean
    @Profile("!local")
    PlansFacade plansRealFacade(DaysPlansRepository repository, AvailableMealsRepository availableMealsRepository) {
        return new PlansFacade(repository, availableMealsRepository);
    }
}
