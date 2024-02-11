package pl.company.foodatu.meals.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.company.foodatu.meals.dto.MealEvent;
import pl.company.foodatu.meals.dto.RestMealResponse;
import pl.company.foodatu.meals.infrastructure.MealPublisher;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

class InMemoryMealPublisher implements MealPublisher {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealPublisher.class);

    private Queue<MealEvent> memory = new ArrayBlockingQueue<>(5);

    @Override
    public void publishNewMeal(MealEvent mealEvent) {
        memory.offer(mealEvent);
        logger.info("Published meal: {}", mealEvent.name());
    }

    MealEvent pollNextPublishedMeal() {
        return memory.poll();
    }
}
