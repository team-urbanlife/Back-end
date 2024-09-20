package com.wegotoo.domain.accompany.repository;

import static com.wegotoo.domain.accompany.QAccompany.accompany;
import static com.wegotoo.domain.user.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wegotoo.domain.accompany.repository.response.AccompanyFindAllQueryEntity;
import com.wegotoo.domain.accompany.repository.response.QAccompanyFindAllQueryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccompanyRepositoryImpl implements AccompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AccompanyFindAllQueryEntity> accompanyFindAll(Integer offset, Integer size) {
        return queryFactory.select(new QAccompanyFindAllQueryEntity(
                accompany.id,
                accompany.startDate,
                accompany.endDate,
                accompany.title,
                accompany.content,
                user.name,
                accompany.registeredDateTime
        ))
                .from(accompany)
                .join(accompany.user, user)
                .offset(offset)
                .limit(size + 1)
                .groupBy(accompany.id)
                .orderBy(accompany.registeredDateTime.desc())
                .fetch();
    }
}