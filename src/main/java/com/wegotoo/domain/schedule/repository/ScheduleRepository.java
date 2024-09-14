package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.Schedule;
import com.wegotoo.domain.schedule.repository.querydsl.ScheduleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {

}
