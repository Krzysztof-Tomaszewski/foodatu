package pl.company.foodatu.plans.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryAvailableMealsRepository implements AvailableMealsRepository {

    Map<UUID, AvailableMeal> memory = new HashMap<>();

    @Override
    public AvailableMeal save(AvailableMeal meal) {
        if(meal.getId() == null) {
            UUID uuid = UUID.randomUUID();
            meal.setId(uuid);
        }
        memory.put(meal.getId(), meal);
        return meal;
    }


    @Override
    public Optional<AvailableMeal> findById(UUID id) {
        return Optional.ofNullable(memory.get(id));
    }


}
