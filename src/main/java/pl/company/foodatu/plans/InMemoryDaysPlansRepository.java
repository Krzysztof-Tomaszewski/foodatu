package pl.company.foodatu.plans;

import java.time.LocalDate;
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
                        memory.getOrDefault(plan.user(), List.of())
                                .stream()
                                .filter(it -> !it.localDate().isEqual(plan.localDate())),
                        Stream.of(plan)
                )
                .toList();
        memory.put(new UserId(plan.user().getId()), plans);
        return plan;
    }

    @Override
    public Optional<DayPlan> find(UserId user, LocalDate date) {
        return memory.getOrDefault(user, List.of()).stream().filter(it -> it.localDate().isEqual(date)).findFirst();
    }
}
