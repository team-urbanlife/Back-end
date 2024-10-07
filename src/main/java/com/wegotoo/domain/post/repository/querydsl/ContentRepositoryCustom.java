package com.wegotoo.domain.post.repository.querydsl;

import com.wegotoo.domain.post.repository.response.ContentImageQueryEntity;
import com.wegotoo.domain.post.repository.response.ContentTextQueryEntity;
import java.util.List;

public interface ContentRepositoryCustom {
    List<ContentTextQueryEntity> findAllPostWithContentText(List<Long> postIds);

    List<ContentImageQueryEntity> findAllPostWithContentImage(List<Long> postIds);
}
