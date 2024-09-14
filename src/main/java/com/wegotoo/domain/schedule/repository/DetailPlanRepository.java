package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.querydsl.DetailPlanRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPlanRepository extends JpaRepository<DetailedPlan, Long>, DetailPlanRepositoryCustom {

    Optional<DetailedPlan> findTopBySequenceLessThanOrderBySequenceDesc(Long sequence);

    Optional<DetailedPlan> findTopBySequenceGreaterThanOrderBySequenceAsc(Long sequence);

    @Query("SELECT dp FROM DetailedPlan dp JOIN FETCH dp.scheduleDetails sd WHERE sd IN :scheduleDetails")
    List<DetailedPlan> findByScheduleDetails(@Param("scheduleDetails") List<ScheduleDetails> scheduleDetails);

}
