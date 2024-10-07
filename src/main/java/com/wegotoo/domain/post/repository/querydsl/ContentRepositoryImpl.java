package com.wegotoo.domain.post.repository.querydsl;

import static com.wegotoo.domain.post.QContent.content;
import static com.wegotoo.domain.post.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.post.ContentType;
import com.wegotoo.domain.post.repository.response.ContentImageQueryEntity;
import com.wegotoo.domain.post.repository.response.ContentTextQueryEntity;
import com.wegotoo.domain.post.repository.response.QContentImageQueryEntity;
import com.wegotoo.domain.post.repository.response.QContentTextQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentRepositoryImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ContentTextQueryEntity> findAllPostWithContentText(List<Long> postIds) {
        return queryFactory.select(new QContentTextQueryEntity(
                        post.id,
                        content.id,
                        content.text
                ))
                .from(content)
                .join(content.post, post)
                .where(content.post.id.in(postIds).and(content.type.eq(ContentType.T)))
                .groupBy(post.id, content.id)
                .fetch();
    }

    @Override
    public List<ContentImageQueryEntity> findAllPostWithContentImage(List<Long> postIds) {
        return queryFactory.select(new QContentImageQueryEntity(
                        post.id,
                        content.id,
                        content.text
                ))
                .from(content)
                .join(content.post, post)
                .where(content.post.id.in(postIds).and(content.type.eq(ContentType.IMAGE)))
                .groupBy(post.id, content.id)
                .fetch();
    }

}
