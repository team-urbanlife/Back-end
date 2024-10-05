package com.wegotoo.domain.post.repository;

import com.wegotoo.domain.post.Content;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByIdIn(List<Long> contentIds);
}
