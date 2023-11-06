package pl.company.foodatu.meals.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class MealsConfiguration {
    @Bean
    @Profile("local")
    MealsFacade mealsInMemoryFacade() {
        return new MealsFacade(new InMemoryMealsRepository(), new InMemoryStdProductsRepository());
    }

    @Bean
    @Profile("!local")
    MealsFacade mealsRealFacade(MealsRepository repository, StdProductsRepository stdProductsRepository) {
        return new MealsFacade(repository, stdProductsRepository);
    }
}
