package pl.company.foodatu.plans.domain;

import java.util.Optional;

interface AvailableMealsRepository {

    AvailableMeal save(AvailableMeal meal);

    Optional<AvailableMeal> findById(String uuid);

}
