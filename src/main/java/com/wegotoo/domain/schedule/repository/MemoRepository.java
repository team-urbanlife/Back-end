package com.wegotoo.domain.schedule.repository;

import com.wegotoo.domain.schedule.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

}
