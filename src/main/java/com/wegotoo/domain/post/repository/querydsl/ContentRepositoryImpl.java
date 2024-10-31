package com.wegotoo.domain.post.repository.querydsl;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.wegotoo.domain.post.QContent.content;
import static com.wegotoo.domain.post.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.post.repository.response.ContentQueryEntity;
import com.wegotoo.domain.post.repository.response.QContentQueryEntity;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContentRepositoryImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<ContentQueryEntity>> findAllByPostIds(List<Long> postIds) {
        return queryFactory.from(content)
                .join(content.post, post)
                .where(content.post.id.in(postIds))
                .transform(groupBy(post.id).as(list(new QContentQueryEntity(
                                post.id,
                                content.id,
                                content.text,
                                content.type
                        )
                )));
    }

}
