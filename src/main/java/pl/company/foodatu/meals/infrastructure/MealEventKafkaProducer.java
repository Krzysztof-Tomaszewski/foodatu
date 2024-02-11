package pl.company.foodatu.meals.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.company.foodatu.meals.dto.MealEvent;

@Component
class MealEventKafkaProducer implements MealPublisher {

    private static final Logger logger = LoggerFactory.getLogger(MealEventKafkaProducer.class);

    private final KafkaTemplate<String, MealEvent> kafkaTemplate;

    private final String topic;

    MealEventKafkaProducer(KafkaTemplate<String, MealEvent> kafkaTemplate,
                           @Value("${foodatu.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishNewMeal(MealEvent meal) {
        this.kafkaTemplate.send(topic, meal.id().toString(), meal);
        logger.info("Kafka producer sent new meal [{}] to topic {}", meal.name(), topic);
    }

}
