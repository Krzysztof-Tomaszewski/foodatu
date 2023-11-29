package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface MealsRepository {

    Meal save(Meal meal);

    Page<Meal> findAll(Pageable pageable);
}
