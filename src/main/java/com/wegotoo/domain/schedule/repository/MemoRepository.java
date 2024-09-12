package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.DetailedPlan;
import com.wegotoo.domain.schedule.Memo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    void deleteByDetailedPlan(DetailedPlan detailedPlan);
}
