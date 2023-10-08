package pl.company.foodatu.plans;

import java.time.LocalDate;
import java.util.Optional;

interface DaysPlansRepository {

    DayPlan save(DayPlan plan);
    Optional<DayPlan> find(UserId user, LocalDate date);

}
