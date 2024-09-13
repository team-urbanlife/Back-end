package com.wegotoo.domain.schedule.repository.querydsl;

import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import java.util.List;

public interface ScheduleDetailsRepositoryCustom {
    List<ScheduleDetailsQueryEntity> findScheduleDetails(Long scheduleId);
}
