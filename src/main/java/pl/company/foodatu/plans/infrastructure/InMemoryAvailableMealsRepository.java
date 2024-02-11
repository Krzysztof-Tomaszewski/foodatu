package pl.company.foodatu.plans.infrastructure;

import pl.company.foodatu.plans.domain.AvailableMeal;
import pl.company.foodatu.plans.domain.AvailableMealsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAvailableMealsRepository implements AvailableMealsRepository {

    Map<UUID, AvailableMeal> memory = new HashMap<>();

    @Override
    public AvailableMeal save(AvailableMeal meal) {
        memory.put(meal.getId(), meal);
        return meal;
    }


    @Override
    public Optional<AvailableMeal> findById(String id) {
        return Optional.ofNullable(memory.get(UUID.fromString(id)));
    }


}
