package pl.company.foodatu.meals.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class MealsConfiguration {
    @Bean
    @Profile("local")
    MealsFacade inMemoryFacade() {
        return new MealsFacade(new InMemoryMealsRepository());
    }

    @Bean
    @Profile("!local")
    MealsFacade realFacade(MealsRepository repository) {
        return new MealsFacade(repository);
    }
}
