package com.wegotoo.domain.post.repository.querydsl;

import static com.wegotoo.domain.post.QPost.post;
import static com.wegotoo.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.post.repository.response.PostQueryEntity;
import com.wegotoo.domain.post.repository.response.QPostQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostQueryEntity> findAllPost(Integer offset, Integer size) {
        return queryFactory.select(new QPostQueryEntity(
                post.id,
                post.title,
                user.name,
                user.profileImage,
                post.registeredDateTime
        ))
                .from(post)
                .join(post.user, user)
                .offset(offset)
                .limit(size + 1)
                .groupBy(post.id)
                .orderBy(post.registeredDateTime.desc())
                .fetch();
    }

}
