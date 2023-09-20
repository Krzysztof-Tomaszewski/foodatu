package pl.company.foodatu.plans;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
interface SpringDayPlansRepository extends DaysPlansRepository, CrudRepository<DayPlan, Long> {

    @Query("SELECT d FROM DayPlan d WHERE d.user = :userId.id) AND d.localDate = :date")
    Optional<DayPlan> find(@Param("userId") UserId user, @Param("date") LocalDate date);
}
