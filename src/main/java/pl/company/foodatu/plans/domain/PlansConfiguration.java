package pl.company.foodatu.plans.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class PlansConfiguration {

    @Bean
    @Profile("local")
    PlansFacade plansInMemoryFacade() {
        return new PlansFacade(new InMemoryDaysPlansRepository());
    }

    @Bean
    @Profile("!local")
    PlansFacade plansRealFacade(DaysPlansRepository repository) {
        return new PlansFacade(repository);
    }
}
