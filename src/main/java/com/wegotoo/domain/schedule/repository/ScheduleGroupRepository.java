package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.ScheduleGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, Long> {

    Optional<ScheduleGroup> findByUserIdAndScheduleId(Long userId, Long scheduleId);

    Optional<ScheduleGroup> findBySchedule(Schedule schedule);
}
