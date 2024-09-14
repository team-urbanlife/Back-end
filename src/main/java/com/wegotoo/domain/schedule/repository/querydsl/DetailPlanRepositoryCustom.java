package com.wegotoo.domain.schedule.repository.querydsl;

import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import java.time.LocalDate;
import java.util.List;

public interface DetailPlanRepositoryCustom {
    Long dayIncludedPlanCount(Long scheduleId, LocalDate date);

    List<DetailedPlanQueryEntity> findDetailedPlans(List<Long> scheduleDetailsIds);
}
