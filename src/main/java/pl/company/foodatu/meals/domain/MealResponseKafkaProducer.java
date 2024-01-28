package pl.company.foodatu.meals.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import pl.company.foodatu.meals.dto.MealResponse;

@Component
public class MealResponseKafkaProducer {

    private final KafkaTemplate<String, MealResponse> kafkaTemplate;

    @Value("${foodatu.kafka.topic}")
    private String topic;

    MealResponseKafkaProducer(KafkaTemplate<String, MealResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MealResponse meal) {
        this.kafkaTemplate.send(topic,meal.id().toString(), meal);
        System.out.println("Sent sample message [" + meal.name() + "] to " + topic);
    }

}
