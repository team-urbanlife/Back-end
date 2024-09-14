package com.wegotoo.domain.schedule.repository.querydsl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.wegotoo.domain.schedule.QDetailedPlan.detailedPlan;
import static com.wegotoo.domain.schedule.QScheduleDetails.scheduleDetails;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.schedule.repository.response.DetailedPlanQueryEntity;
import com.wegotoo.domain.schedule.repository.response.QDetailedPlanQueryEntity;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DetailPlanRepositoryImpl implements DetailPlanRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long dayIncludedPlanCount(Long scheduleDetailsId, LocalDate date) {
        Long count = queryFactory.select(detailedPlan.sequence.max())
                .from(detailedPlan)
                .join(detailedPlan.scheduleDetails, scheduleDetails)
                .where(scheduleDetails.id.eq(scheduleDetailsId)
                        .and(scheduleDetails.date.eq(date)))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public List<DetailedPlanQueryEntity> findDetailedPlans(List<Long> scheduleDetailsIds) {
        return queryFactory.select(new QDetailedPlanQueryEntity(
                detailedPlan.name,
                detailedPlan.sequence,
                detailedPlan.latitude,
                detailedPlan.longitude,
                detailedPlan.scheduleDetails.id
        ))
                .from(detailedPlan)
                .join(detailedPlan.scheduleDetails, scheduleDetails)
                .where(detailedPlan.scheduleDetails.id.in(scheduleDetailsIds))
                .fetch();
    }

}
