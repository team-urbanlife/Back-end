package com.wegotoo.domain.post.repository;

import com.wegotoo.domain.post.Content;
import com.wegotoo.domain.post.repository.querydsl.ContentRepositoryCustom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {

    List<Content> findByIdIn(List<Long> contentIds);
}
