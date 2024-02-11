package pl.company.foodatu.meals.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.company.foodatu.meals.dto.MealEvent;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class InMemoryMealPublisher implements MealPublisher {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealPublisher.class);

    private Queue<MealEvent> memory = new ArrayBlockingQueue<>(5);

    @Override
    public void publishNewMeal(MealEvent mealEvent) {
        memory.offer(mealEvent);
        logger.info("Published meal: {}", mealEvent.name());
    }

    public MealEvent pollNextPublishedMeal() {
        return memory.poll();
    }
}
