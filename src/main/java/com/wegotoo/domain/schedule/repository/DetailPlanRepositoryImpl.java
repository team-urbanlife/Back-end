package com.wegotoo.domain.schedule.repository;

import static com.wegotoo.domain.schedule.QDetailedPlan.detailedPlan;
import static com.wegotoo.domain.schedule.QScheduleDetails.scheduleDetails;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DetailPlanRepositoryImpl implements DetailPlanRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long dayIncludedPlanCount(Long scheduleDetailsId, LocalDate date) {
        Long count = queryFactory.select(detailedPlan.count())
                .from(detailedPlan)
                .join(detailedPlan.scheduleDetails, scheduleDetails)
                .where(scheduleDetails.id.eq(scheduleDetailsId)
                        .and(scheduleDetails.date.eq(date)))
                .fetchOne();

        return count != null ? count : 0L;
    }

}
