package pl.company.foodatu.meals.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SpringMealsRepository extends CrudRepository<Meal, String>, MealsRepository {
}
