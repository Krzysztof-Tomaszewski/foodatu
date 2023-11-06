package pl.company.foodatu.meals.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SpringMealsRepository extends CrudRepository<Meal, UUID>, MealsRepository {
}
