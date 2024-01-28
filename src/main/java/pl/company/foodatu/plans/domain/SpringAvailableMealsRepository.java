package pl.company.foodatu.plans.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SpringAvailableMealsRepository extends CrudRepository<AvailableMeal, UUID>, AvailableMealsRepository{
}
