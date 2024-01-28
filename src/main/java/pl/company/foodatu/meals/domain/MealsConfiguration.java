package pl.company.foodatu.meals.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class MealsConfiguration {
    @Bean
    @Profile("local")
    MealsFacade mealsInMemoryFacade(MealResponseKafkaProducer mealResponseKafkaProducer) {
        return new MealsFacade(new InMemoryMealsRepository(), new InMemoryStdProductsRepository(), mealResponseKafkaProducer);
    }

    @Bean
    @Profile("!local")
    MealsFacade mealsRealFacade(MealsRepository repository, StdProductsRepository stdProductsRepository, MealResponseKafkaProducer mealResponseKafkaProducer) {
        return new MealsFacade(repository, stdProductsRepository, mealResponseKafkaProducer);
    }
}
