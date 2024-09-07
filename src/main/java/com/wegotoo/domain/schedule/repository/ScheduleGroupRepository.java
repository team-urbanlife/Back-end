package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.ScheduleGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleGroupRepository extends JpaRepository<ScheduleGroup, Long> {

    Optional<Object> findByUserIdAndScheduleId(Long userId, Long scheduleId);

}
