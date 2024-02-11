package pl.company.foodatu.meals.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.company.foodatu.meals.infrastructure.MealPublisher;

@Configuration
class MealsConfiguration {
    @Bean
    @Profile("local")
    MealsFacade mealsInMemoryFacade(MealPublisher mealPublisher) {
        return new MealsFacade(new InMemoryMealsRepository(), new InMemoryStdProductsRepository(), mealPublisher);
    }

    @Bean
    @Profile("!local")
    MealsFacade mealsRealFacade(MealsRepository repository, StdProductsRepository stdProductsRepository, MealPublisher mealPublisher) {
        return new MealsFacade(repository, stdProductsRepository, mealPublisher);
    }
}
