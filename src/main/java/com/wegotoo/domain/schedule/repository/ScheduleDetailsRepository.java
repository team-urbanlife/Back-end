package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import com.wegotoo.domain.schedule.repository.querydsl.ScheduleDetailsRepositoryCustom;
import com.wegotoo.domain.schedule.repository.querydsl.ScheduleRepositoryCustom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDetailsRepository extends JpaRepository<ScheduleDetails, Long>, ScheduleDetailsRepositoryCustom {

    ScheduleDetails findByDateAndSchedule(LocalDate date, Schedule schedule);

    Optional<ScheduleDetails> findByIdAndDate(Long scheduleDetailsId, LocalDate date);

    @Query("SELECT sd FROM ScheduleDetails sd JOIN FETCH sd.schedule WHERE sd.schedule = :schedule")
    List<ScheduleDetails> findBySchedule(@Param("schedule") Schedule schedule);

    void deleteAllBySchedule(Schedule schedule);
}
