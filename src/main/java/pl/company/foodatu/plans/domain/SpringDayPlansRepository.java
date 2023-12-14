package pl.company.foodatu.plans.domain;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.company.foodatu.plans.dto.UserId;

import java.time.LocalDate;
import java.util.Optional;

@Repository
interface SpringDayPlansRepository extends DaysPlansRepository, CrudRepository<DayPlan, Long> {

    @Query("SELECT d FROM DayPlan d WHERE d.user.id = :#{#user.id} AND d.localDate = :date")
    Optional<DayPlan> find(@Param("user") UserId user, @Param("date") LocalDate date);
}
