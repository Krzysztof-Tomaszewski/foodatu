package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

interface MealsRepository {

    Meal save(Meal meal);

    Page<Meal> findAll(Pageable pageable);

    Optional<Meal> findById(String id);
}
