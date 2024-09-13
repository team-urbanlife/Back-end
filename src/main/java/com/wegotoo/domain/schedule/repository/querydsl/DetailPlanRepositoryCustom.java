package com.wegotoo.domain.schedule.repository.querydsl;

import java.time.LocalDate;

public interface DetailPlanRepositoryCustom {
    Long dayIncludedPlanCount(Long scheduleId, LocalDate date);
}
