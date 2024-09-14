package com.wegotoo.domain.schedule.repository.querydsl;

import com.wegotoo.application.schedule.response.ScheduleFindAllResponse;
import com.wegotoo.domain.schedule.repository.response.ScheduleQueryEntity;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleQueryEntity> findAllSchedules(Long userId, Integer offset, Integer size);
}
