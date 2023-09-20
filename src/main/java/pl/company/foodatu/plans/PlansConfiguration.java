package pl.company.foodatu.plans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class PlansConfiguration {

    @Bean
    @Profile("local")
    PlansFacade inMemoryFacade() {
        return new PlansFacade(new InMemoryDaysPlansRepository());
    }

    @Bean
    @Profile("!local")
    PlansFacade realFacade(DaysPlansRepository repository) {
        return new PlansFacade(repository);
    }
}
