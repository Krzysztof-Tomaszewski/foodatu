package pl.company.foodatu.meals.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.company.foodatu.meals.dto.MealResponse;
import pl.company.foodatu.meals.infrastructure.MealPublisher;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

class InMemoryMealPublisher implements MealPublisher {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealPublisher.class);

    private Queue<MealResponse> memory = new ArrayBlockingQueue<>(5);

    @Override
    public void publishNewMeal(MealResponse mealResponse) {
        memory.offer(mealResponse);
        logger.info("Published meal: {}", mealResponse.name());
    }

    MealResponse pollNextPublishedMeal() {
        return memory.poll();
    }
}
