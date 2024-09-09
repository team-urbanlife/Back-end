package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.DetailedPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPlanRepository extends JpaRepository<DetailedPlan, Long>, DetailPlanRepositoryCustom {

    Optional<DetailedPlan> findTopBySequenceLessThanOrderBySequenceDesc(Long sequence);

    Optional<DetailedPlan> findTopBySequenceGreaterThanOrderBySequenceAsc(Long sequence);
}
