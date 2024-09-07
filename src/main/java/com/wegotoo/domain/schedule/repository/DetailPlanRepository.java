package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.DetailedPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPlanRepository extends JpaRepository<DetailedPlan, Long>, DetailPlanRepositoryCustom {

}
