package com.wegotoo.domain.like.repository;

import com.wegotoo.domain.like.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserIdAndPostId(Long userId, Long postId);

    @Query("select pl.post.id from PostLike pl where pl.user.id = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") Long userId);
}
