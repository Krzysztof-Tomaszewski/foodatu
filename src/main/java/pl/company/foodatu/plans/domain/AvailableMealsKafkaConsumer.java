package pl.company.foodatu.plans.domain;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import pl.company.foodatu.meals.dto.MealResponse;

import java.util.List;

@Component
class AvailableMealsKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AvailableMealsKafkaConsumer.class);
    private final AvailableMealsRepository availableMealsRepository;

    AvailableMealsKafkaConsumer(AvailableMealsRepository availableMealsRepository) {
        this.availableMealsRepository = availableMealsRepository;
    }

    @KafkaListener(topics = "${foodatu.kafka.topic}")
    void processMeal(MealResponse meal,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) List<String> topics,
                                    @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        logger.info("New Kafka event: {}-{}[{}] {}", topics.get(0), partitions.get(0), offsets.get(0), meal.name());
        availableMealsRepository.save(new AvailableMeal(meal.id(), meal.name(), meal.nutritionValues().carbons(), meal.nutritionValues().proteins(), meal.nutritionValues().fat()));
    }

}
