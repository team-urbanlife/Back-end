package com.wegotoo.domain.schedule.repository.querydsl;

import static com.wegotoo.domain.schedule.QSchedule.schedule;
import static com.wegotoo.domain.schedule.QScheduleDetails.scheduleDetails;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.schedule.repository.response.QScheduleDetailsQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleDetailsQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleDetailsRepositoryImpl implements ScheduleDetailsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleDetailsQueryEntity> findScheduleDetails(Long scheduleId) {
        return queryFactory.select(new QScheduleDetailsQueryEntity(
                scheduleDetails.id,
                scheduleDetails.date
        ))
                .from(scheduleDetails)
                .join(scheduleDetails.schedule, schedule)
                .where(scheduleDetails.schedule.id.eq(scheduleId))
                .fetch();
    }
}
