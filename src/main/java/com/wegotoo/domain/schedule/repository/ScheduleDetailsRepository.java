package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.ScheduleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDetailsRepository extends JpaRepository<ScheduleDetails, Long>{

}
