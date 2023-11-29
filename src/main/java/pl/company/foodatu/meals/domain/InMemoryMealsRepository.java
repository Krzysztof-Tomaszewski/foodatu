package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class InMemoryMealsRepository implements MealsRepository {

    Map<UUID, Meal> memory = new HashMap<>();

    @Override
    public Meal save(Meal meal) {
        UUID uuid = UUID.randomUUID();
        meal.setId(uuid);
        memory.put(uuid, meal);
        return meal;
    }

    @Override
    public Page<Meal> findAll(Pageable pageable) {
        List<Meal> meals = memory.values().stream().toList();
        return new PageImpl<>(meals, pageable, meals.size());
    }
}
