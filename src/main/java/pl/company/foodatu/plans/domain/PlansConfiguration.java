package pl.company.foodatu.plans.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.company.foodatu.plans.infrastructure.InMemoryAvailableMealsRepository;

@Configuration
class PlansConfiguration {

    @Bean
    @Profile("local")
    PlansFacade plansInMemoryFacade() {
        return new PlansFacade(new InMemoryDaysPlansRepository(), new InMemoryAvailableMealsRepository());
    }

    @Bean
    @Profile("!local")
    PlansFacade plansRealFacade(DaysPlansRepository repository, AvailableMealsRepository availableMealsRepository) {
        return new PlansFacade(repository, availableMealsRepository);
    }
}
