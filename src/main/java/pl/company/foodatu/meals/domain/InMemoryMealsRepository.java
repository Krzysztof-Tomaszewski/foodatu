package pl.company.foodatu.meals.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class InMemoryMealsRepository implements MealsRepository{

    Map<UUID, Meal> memory = new HashMap<>();

    @Override
    public Meal save(Meal meal) {
        UUID uuid = UUID.randomUUID();
        meal.setId(uuid);
        memory.put(uuid, meal);
        return meal;
    }

    @Override
    public List<Meal> findAll() {
        return memory.values().stream().toList();
    }
}
