package pl.company.foodatu.plans.domain;

import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.Optional;

interface DaysPlansRepository {

    DayPlan save(DayPlan plan);

    Optional<DayPlan> find(UserId user, LocalDate date);

}
