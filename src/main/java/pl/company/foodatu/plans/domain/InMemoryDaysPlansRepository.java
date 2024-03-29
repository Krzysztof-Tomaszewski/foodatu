package pl.company.foodatu.plans.domain;

import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class InMemoryDaysPlansRepository implements DaysPlansRepository {

    private Map<UserId, List<DayPlan>> memory = new HashMap<>();

    @Override
    public DayPlan save(DayPlan plan) {
        var plans = Stream.concat(
                        memory.getOrDefault(new UserId(plan.userId()), List.of())
                                .stream()
                                .filter(dayPlan -> !dayPlan.localDate().isEqual(plan.localDate())),
                        Stream.of(plan)
                )
                .toList();
        memory.put(new UserId(plan.userId()), plans);
        return plan;
    }

    @Override
    public Optional<DayPlan> find(UserId user, LocalDate date) {
        return memory.getOrDefault(user, new ArrayList<>()).stream()
                .filter(dayPlan -> dayPlan.localDate().isEqual(date)).findFirst();
    }
}
