package pl.company.foodatu.meals.domain;

import java.util.List;

interface MealsRepository {

    Meal save(Meal meal);

    List<Meal> findAll();
}
