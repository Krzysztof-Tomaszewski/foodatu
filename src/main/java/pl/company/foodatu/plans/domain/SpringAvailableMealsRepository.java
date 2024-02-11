package pl.company.foodatu.plans.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SpringAvailableMealsRepository extends CrudRepository<AvailableMeal, String>, AvailableMealsRepository {
}
