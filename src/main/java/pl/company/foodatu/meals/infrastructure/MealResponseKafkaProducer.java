package pl.company.foodatu.meals.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.company.foodatu.meals.dto.MealResponse;

@Component
class MealResponseKafkaProducer implements MealPublisher {

    private static final Logger logger = LoggerFactory.getLogger(MealResponseKafkaProducer.class);

    private final KafkaTemplate<String, MealResponse> kafkaTemplate;

    private final String topic;

    MealResponseKafkaProducer(KafkaTemplate<String, MealResponse> kafkaTemplate,
                              @Value("${foodatu.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publishNewMeal(MealResponse meal) {
        this.kafkaTemplate.send(topic, meal.id().toString(), meal);
        logger.info("Kafka producer sent new meal [{}] to topic {}", meal.name(), topic);
    }

}
