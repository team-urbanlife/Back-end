package com.wegotoo.domain.schedule.repository.querydsl;

import static com.wegotoo.domain.schedule.QSchedule.schedule;
import static com.wegotoo.domain.schedule.QScheduleGroup.scheduleGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.schedule.repository.response.QScheduleQueryEntity;
import com.wegotoo.domain.schedule.repository.response.ScheduleQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleQueryEntity> findAllSchedules(Long userId, Integer offset, Integer size) {
        return queryFactory.select(new QScheduleQueryEntity(
                        schedule.id,
                        schedule.title,
                        schedule.startDate,
                        schedule.endDate,
                        scheduleGroup.count().as("participants")
                ))
                .from(schedule)
                .leftJoin(scheduleGroup).on(schedule.eq(scheduleGroup.schedule))
                .where(scheduleGroup.user.id.eq(userId))
                .offset(offset)
                .limit(size + 1)
                .groupBy(schedule.id)
                .orderBy(schedule.id.desc())
                .fetch();
    }
}
