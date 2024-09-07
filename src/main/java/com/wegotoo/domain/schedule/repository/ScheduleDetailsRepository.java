package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleDetails;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDetailsRepository extends JpaRepository<ScheduleDetails, Long>{

    ScheduleDetails findByDateAndSchedule(LocalDate date, Schedule schedule);
}
