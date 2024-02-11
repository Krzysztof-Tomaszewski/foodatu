package pl.company.foodatu.plans.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import pl.company.foodatu.meals.dto.MealEvent;
import pl.company.foodatu.plans.domain.PlansFacade;

import java.util.List;

@Component
class AvailableMealsKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AvailableMealsKafkaConsumer.class);
    private final PlansFacade plansFacade;

    AvailableMealsKafkaConsumer(PlansFacade plansFacade) {
        this.plansFacade = plansFacade;
    }

    @KafkaListener(topics = "${foodatu.kafka.topic}")
    void processMeal(MealEvent meal,
                     @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                     @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                     @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        logger.info("New Kafka event: {}-{}[{}] {}", topics.get(0), partitions.get(0), offsets.get(0), meal.name());
        plansFacade.addAvailableMeal(meal);
    }

}
