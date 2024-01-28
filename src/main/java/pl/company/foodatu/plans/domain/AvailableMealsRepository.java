package pl.company.foodatu.plans.domain;

import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

interface AvailableMealsRepository {

    AvailableMeal save(AvailableMeal meal);

    Optional<AvailableMeal> findById(UUID uuid);

}
